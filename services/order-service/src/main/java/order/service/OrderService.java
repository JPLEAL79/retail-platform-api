package order.service;

import order.client.CustomerClient;
import order.client.ProductClient;
import order.dto.request.OrderItemRequest;
import order.dto.request.OrderRequest;
import order.entity.DeliveryAddress;
import order.entity.Order;
import order.entity.OrderItem;
import order.entity.OrderStatus;
import common.exception.ConflictException;
import common.exception.ResourceNotFoundException;
import order.mapper.OrderMapper;
import order.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;

    public OrderService(OrderRepository repository, CustomerClient customerClient, ProductClient productClient) {
        this.repository = repository;
        this.customerClient = customerClient;
        this.productClient = productClient;
    }

    @Transactional
    public Order create(OrderRequest request) {
        ensureActiveCustomer(request.getCustomerId());

        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setStatus(resolveInitialStatus(request.getStatus()));
        order.setItems(buildItems(order, request.getItems()));
        order.setDeliveryAddress(buildAddress(order, request));
        order.setTotal(calculateTotal(order.getItems()));
        Map<Long, Integer> stockChanges = buildStockChanges(List.of(), false, order.getItems(), true);

        applyStockChanges(stockChanges);

        try {
            return repository.save(order);
        } catch (RuntimeException exception) {
            rollbackStockChanges(stockChanges);
            throw exception;
        }
    }

    public Page<Order> getAll(Long customerId, OrderStatus status, int page, int size) {
        Pageable pageable = buildPageable(page, size);

        if (customerId != null && status != null) {
            return repository.findByCustomerIdAndStatusOrderByIdAsc(customerId, status, pageable);
        }

        if (customerId != null) {
            return repository.findByCustomerIdOrderByIdAsc(customerId, pageable);
        }

        if (status != null) {
            return repository.findByStatusOrderByIdAsc(status, pageable);
        }

        return repository.findAllByOrderByIdAsc(pageable);
    }

    public Order getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id + "."));
    }

    @Transactional
    public Order update(Long id, OrderRequest request) {
        ensureActiveCustomer(request.getCustomerId());

        Order existingOrder = getById(id);
        OrderStatus updatedStatus = resolveUpdatedStatus(existingOrder.getStatus(), request.getStatus());

        if (updatedStatus == OrderStatus.CANCELED && existingOrder.getStatus() != OrderStatus.CANCELED) {
            Map<Long, Integer> stockChanges = buildStockChanges(existingOrder.getItems(), true, List.of(), false);
            applyStockChanges(stockChanges);

            try {
                existingOrder.setStatus(OrderStatus.CANCELED);
                return repository.save(existingOrder);
            } catch (RuntimeException exception) {
                rollbackStockChanges(stockChanges);
                throw exception;
            }
        }

        List<OrderItem> updatedItems = buildItems(existingOrder, request.getItems());
        Map<Long, Integer> stockChanges = buildStockChanges(existingOrder.getItems(), isStockReserved(existingOrder.getStatus()), updatedItems, isStockReserved(updatedStatus));
        applyStockChanges(stockChanges);

        try {
            existingOrder.setCustomerId(request.getCustomerId());
            existingOrder.setStatus(updatedStatus);
            existingOrder.getItems().clear();
            existingOrder.getItems().addAll(updatedItems);
            updateAddress(existingOrder, request);
            existingOrder.setTotal(calculateTotal(existingOrder.getItems()));
            return repository.save(existingOrder);
        } catch (RuntimeException exception) {
            rollbackStockChanges(stockChanges);
            throw exception;
        }
    }

    @Transactional
    public void delete(Long id) {
        Order order = getById(id);

        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.CANCELED) {
            throw new ConflictException("Only CREATED or CANCELED orders can be deleted.");
        }

        Map<Long, Integer> stockChanges = buildStockChanges(order.getItems(), isStockReserved(order.getStatus()), List.of(), false);
        applyStockChanges(stockChanges);

        try {
            repository.delete(order);
        } catch (RuntimeException exception) {
            rollbackStockChanges(stockChanges);
            throw exception;
        }
    }

    private List<OrderItem> buildItems(Order order, List<OrderItemRequest> itemRequests) {
        List<OrderItem> items = new ArrayList<>();
        Set<Long> productIds = new HashSet<>();

        for (OrderItemRequest itemRequest : itemRequests) {
            if (!productIds.add(itemRequest.getProductId())) {
                throw new ConflictException("Order cannot contain duplicated product ids.");
            }

            ProductClient.ProductResponse product = productClient.getById(itemRequest.getProductId());

            if (!Boolean.TRUE.equals(product.active())) {
                throw new ConflictException("Product " + itemRequest.getProductId() + " is inactive.");
            }

            BigDecimal subtotal = product.price().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(itemRequest.getProductId());
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(product.price());
            item.setSubtotal(subtotal);
            items.add(item);
        }

        return items;
    }

    private OrderStatus resolveInitialStatus(OrderStatus requestedStatus) {
        if (requestedStatus == null) {
            return OrderStatus.CREATED;
        }

        if (requestedStatus != OrderStatus.CREATED) {
            throw new ConflictException("New orders must start with CREATED status.");
        }

        return requestedStatus;
    }

    private OrderStatus resolveUpdatedStatus(OrderStatus currentStatus, OrderStatus requestedStatus) {
        if (requestedStatus == null || requestedStatus == currentStatus) {
            return currentStatus;
        }

        // Orders follow a simple forward-only lifecycle, except cancellation before shipping.
        if (!isAllowedTransition(currentStatus, requestedStatus)) {
            throw new ConflictException("Invalid order status transition from " + currentStatus + " to " + requestedStatus + ".");
        }

        return requestedStatus;
    }

    private boolean isStockReserved(OrderStatus status) {
        return status != OrderStatus.CANCELED;
    }

    private Map<Long, Integer> buildStockChanges(
            List<OrderItem> currentItems,
            boolean currentReserved,
            List<OrderItem> updatedItems,
            boolean updatedReserved
    ) {
        Map<Long, Integer> stockChanges = new HashMap<>();

        if (currentReserved) {
            mergeStockQuantities(stockChanges, currentItems, 1);
        }

        if (updatedReserved) {
            mergeStockQuantities(stockChanges, updatedItems, -1);
        }

        return stockChanges;
    }

    private void mergeStockQuantities(Map<Long, Integer> stockChanges, List<OrderItem> items, int multiplier) {
        for (OrderItem item : items) {
            stockChanges.merge(item.getProductId(), item.getQuantity() * multiplier, Integer::sum);
        }
    }

    private void applyStockChanges(Map<Long, Integer> stockChanges) {
        List<Map.Entry<Long, Integer>> appliedChanges = new ArrayList<>();

        try {
            applyStockChangesByDirection(stockChanges, appliedChanges, true);
            applyStockChangesByDirection(stockChanges, appliedChanges, false);
        } catch (RuntimeException exception) {
            rollbackAppliedChanges(appliedChanges);
            throw exception;
        }
    }

    private void applyStockChangesByDirection(
            Map<Long, Integer> stockChanges,
            List<Map.Entry<Long, Integer>> appliedChanges,
            boolean releasingStock
    ) {
        for (Map.Entry<Long, Integer> entry : stockChanges.entrySet()) {
            int delta = entry.getValue();

            if (delta == 0 || (releasingStock != (delta > 0))) {
                continue;
            }

            // Releases run first so valid replacements are not blocked by the previous reservation.
            productClient.adjustStock(entry.getKey(), delta);
            appliedChanges.add(Map.entry(entry.getKey(), delta));
        }
    }

    private void rollbackStockChanges(Map<Long, Integer> stockChanges) {
        List<Map.Entry<Long, Integer>> appliedChanges = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : stockChanges.entrySet()) {
            if (entry.getValue() == 0) {
                continue;
            }

            appliedChanges.add(Map.entry(entry.getKey(), entry.getValue()));
        }

        rollbackAppliedChanges(appliedChanges);
    }

    private void rollbackAppliedChanges(List<Map.Entry<Long, Integer>> appliedChanges) {
        for (int index = appliedChanges.size() - 1; index >= 0; index--) {
            Map.Entry<Long, Integer> entry = appliedChanges.get(index);

            try {
                productClient.adjustStock(entry.getKey(), -entry.getValue());
            } catch (RuntimeException ignored) {
                // Best-effort rollback is still better than hiding the original business error.
            }
        }
    }

    private boolean isAllowedTransition(OrderStatus currentStatus, OrderStatus requestedStatus) {
        return switch (currentStatus) {
            case CREATED -> requestedStatus == OrderStatus.PAID || requestedStatus == OrderStatus.CANCELED;
            case PAID -> requestedStatus == OrderStatus.PREPARING || requestedStatus == OrderStatus.CANCELED;
            case PREPARING -> requestedStatus == OrderStatus.SHIPPED;
            case SHIPPED -> requestedStatus == OrderStatus.DELIVERED;
            case DELIVERED, CANCELED -> false;
        };
    }

    private void ensureActiveCustomer(Long customerId) {
        CustomerClient.CustomerResponse customer = customerClient.getById(customerId);

        if (!Boolean.TRUE.equals(customer.active())) {
            throw new ConflictException("Customer " + customerId + " is inactive.");
        }
    }

    private DeliveryAddress buildAddress(Order order, OrderRequest request) {
        DeliveryAddress address = OrderMapper.toAddressEntity(request.getDeliveryAddress());
        address.setOrder(order);
        return address;
    }

    private void updateAddress(Order order, OrderRequest request) {
        DeliveryAddress currentAddress = order.getDeliveryAddress();

        if (currentAddress == null) {
            order.setDeliveryAddress(buildAddress(order, request));
            return;
        }

        DeliveryAddress newAddress = OrderMapper.toAddressEntity(request.getDeliveryAddress());
        currentAddress.setRegion(newAddress.getRegion());
        currentAddress.setCity(newAddress.getCity());
        currentAddress.setStreet(newAddress.getStreet());
        currentAddress.setStreetNumber(newAddress.getStreetNumber());
        currentAddress.setApartmentNumber(newAddress.getApartmentNumber());
        currentAddress.setReference(newAddress.getReference());
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Pageable buildPageable(int page, int size) {
        int resolvedPage = Math.max(page, 0);
        int resolvedSize = Math.min(Math.max(size, 1), 100);
        return PageRequest.of(resolvedPage, resolvedSize);
    }
}

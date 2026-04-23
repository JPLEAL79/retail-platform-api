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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;

    public OrderService(OrderRepository repository, CustomerClient customerClient, ProductClient productClient) {
        this.repository = repository;
        this.customerClient = customerClient;
        this.productClient = productClient;
    }

    public Order create(OrderRequest request) {
        ensureActiveCustomer(request.getCustomerId());

        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setStatus(request.getStatus() != null ? request.getStatus() : OrderStatus.CREATED);
        order.setItems(buildItems(order, request.getItems()));
        order.setDeliveryAddress(buildAddress(order, request));
        order.setTotal(calculateTotal(order.getItems()));

        return repository.save(order);
    }

    public List<Order> getAll() {
        return repository.findAll();
    }

    public Order getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id + "."));
    }

    public Order update(Long id, OrderRequest request) {
        ensureActiveCustomer(request.getCustomerId());

        Order existingOrder = getById(id);
        existingOrder.setCustomerId(request.getCustomerId());
        existingOrder.setStatus(request.getStatus() != null ? request.getStatus() : existingOrder.getStatus());
        existingOrder.getItems().clear();
        existingOrder.getItems().addAll(buildItems(existingOrder, request.getItems()));
        updateAddress(existingOrder, request);
        existingOrder.setTotal(calculateTotal(existingOrder.getItems()));

        return repository.save(existingOrder);
    }

    public void delete(Long id) {
        Order order = getById(id);

        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.CANCELED) {
            throw new ConflictException("Only CREATED or CANCELED orders can be deleted.");
        }

        repository.delete(order);
    }

    private List<OrderItem> buildItems(Order order, List<OrderItemRequest> itemRequests) {
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequest itemRequest : itemRequests) {
            ProductClient.ProductResponse product = productClient.getById(itemRequest.getProductId());

            if (!Boolean.TRUE.equals(product.active())) {
                throw new ConflictException("Product " + itemRequest.getProductId() + " is inactive.");
            }

            if (product.stock() < itemRequest.getQuantity()) {
                throw new ConflictException("Product " + itemRequest.getProductId() + " does not have enough stock.");
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
}

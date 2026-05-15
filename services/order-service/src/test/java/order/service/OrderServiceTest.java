package order.service;

import common.exception.ConflictException;
import order.client.CustomerClient;
import order.client.ProductClient;
import order.dto.request.DeliveryAddressRequest;
import order.dto.request.OrderItemRequest;
import order.dto.request.OrderRequest;
import order.entity.Order;
import order.entity.OrderItem;
import order.entity.OrderStatus;
import order.repository.OrderRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    private final OrderRepository repository = mock(OrderRepository.class);
    private final ProductClient productClient = mock(ProductClient.class);
    private final CustomerClient customerClient = mock(CustomerClient.class);
    private final OrderService service = new OrderService(repository, productClient, customerClient);

    // A valid order reserves stock and calculates totals from product-service prices.
    @Test
    void createDiscountsStockAndCalculatesTotal() {
        OrderRequest request = orderRequest(OrderStatus.CREATED, 3L, 2);
        when(productClient.getById(3L)).thenReturn(product(3L, BigDecimal.valueOf(550000), 10));
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order createdOrder = service.create(request);

        assertEquals(OrderStatus.CREATED, createdOrder.getStatus());
        assertEquals(BigDecimal.valueOf(1100000), createdOrder.getTotal());
        verify(customerClient).getById(11L);
        verify(productClient).adjustStock(3L, -2);
    }

    // Duplicated products in the same order would make totals and stock movements ambiguous.
    @Test
    void createRejectsDuplicatedProductsInSameOrder() {
        OrderRequest request = orderRequest(OrderStatus.CREATED, 3L, 2);
        request.getItems().add(item(3L, 1));
        when(productClient.getById(3L)).thenReturn(product(3L, BigDecimal.valueOf(550000), 10));

        assertThrows(ConflictException.class, () -> service.create(request));

        verify(productClient, never()).adjustStock(anyLong(), anyInt());
        verify(repository, never()).save(any(Order.class));
    }

    // Delivered orders are final and cannot move back to canceled.
    @Test
    void updateStatusRejectsCancelDeliveredOrder() {
        Order deliveredOrder = existingOrder(OrderStatus.DELIVERED);
        when(repository.findById(2L)).thenReturn(Optional.of(deliveredOrder));

        assertThrows(ConflictException.class, () -> service.updateStatus(2L, OrderStatus.CANCELED));
    }

    // Paid orders are still active, so they cannot be deleted directly.
    @Test
    void deleteRejectsPaidOrder() {
        Order paidOrder = existingOrder(OrderStatus.PAID);
        when(repository.findById(2L)).thenReturn(Optional.of(paidOrder));

        assertThrows(ConflictException.class, () -> service.delete(2L));
    }

    // Canceling an active order releases the stock that was reserved when it was created.
    @Test
    void cancelOrderReleasesReservedStock() {
        Order paidOrder = existingOrder(OrderStatus.PAID);
        when(repository.findById(2L)).thenReturn(Optional.of(paidOrder));
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order canceledOrder = service.updateStatus(2L, OrderStatus.CANCELED);

        assertEquals(OrderStatus.CANCELED, canceledOrder.getStatus());
        verify(productClient).adjustStock(3L, 2);
    }

    private OrderRequest orderRequest(OrderStatus status, Long productId, int quantity) {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(11L);
        request.setStatus(status);
        request.setItems(new java.util.ArrayList<>(List.of(item(productId, quantity))));
        request.setDeliveryAddress(address());
        return request;
    }

    private OrderItemRequest item(Long productId, int quantity) {
        OrderItemRequest item = new OrderItemRequest();
        item.setProductId(productId);
        item.setQuantity(quantity);
        return item;
    }

    private DeliveryAddressRequest address() {
        DeliveryAddressRequest address = new DeliveryAddressRequest();
        address.setRegion("Metropolitana");
        address.setCity("Santiago");
        address.setStreet("Av Providencia");
        address.setStreetNumber("1234");
        return address;
    }

    private ProductClient.ProductResponse product(Long id, BigDecimal price, int stock) {
        return new ProductClient.ProductResponse(id, "SKU-001", "Notebook Lenovo", "Lenovo", price, stock);
    }

    private Order existingOrder(OrderStatus status) {
        Order order = new Order();
        order.setId(2L);
        order.setCustomerId(11L);
        order.setStatus(status);
        order.setTotal(BigDecimal.valueOf(1100000));

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProductId(3L);
        item.setQuantity(2);
        item.setUnitPrice(BigDecimal.valueOf(550000));
        item.setSubtotal(BigDecimal.valueOf(1100000));
        order.getItems().add(item);

        return order;
    }
}

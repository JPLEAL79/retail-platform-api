package order.dto.response;

import order.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponse {

    private Long id;
    private Long customerId;
    private BigDecimal total;
    private OrderStatus status;
    private List<OrderItemResponse> items;
    private DeliveryAddressResponse deliveryAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    public DeliveryAddressResponse getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddressResponse deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}

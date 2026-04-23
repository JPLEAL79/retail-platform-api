package order.dto.request;

import order.entity.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class OrderRequest {

    @NotNull(message = "Customer id is required.")
    @Positive(message = "Customer id must be greater than zero.")
    private Long customerId;

    @Valid
    @NotEmpty(message = "Order must have at least one item.")
    private List<OrderItemRequest> items;

    @Valid
    @NotNull(message = "Delivery address is required.")
    private DeliveryAddressRequest deliveryAddress;

    private OrderStatus status;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public DeliveryAddressRequest getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddressRequest deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

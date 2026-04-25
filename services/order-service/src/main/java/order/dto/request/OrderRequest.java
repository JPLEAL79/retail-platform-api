package order.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import order.entity.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class OrderRequest {

    @NotNull(message = "Customer id is required.")
    @Positive(message = "Customer id must be greater than zero.")
    @JsonProperty("clienteId")
    @JsonAlias("customerId")
    private Long customerId;

    @Valid
    @NotEmpty(message = "Order must have at least one item.")
    @JsonProperty("detalles")
    @JsonAlias("items")
    private List<OrderItemRequest> items;

    @Valid
    @NotNull(message = "Delivery address is required.")
    @JsonProperty("direccionEntrega")
    @JsonAlias("deliveryAddress")
    private DeliveryAddressRequest deliveryAddress;

    @JsonProperty("estado")
    @JsonAlias("status")
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

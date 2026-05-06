package order.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import order.entity.OrderStatus;

public class OrderStatusRequest {

    @NotNull(message = "Status is required.")
    @JsonProperty("estado")
    @JsonAlias("status")
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

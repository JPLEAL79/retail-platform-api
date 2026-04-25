package product.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class StockAdjustmentRequest {

    @NotNull(message = "Stock delta is required.")
    @JsonProperty("ajusteStock")
    @JsonAlias("delta")
    private Integer delta;

    public Integer getDelta() {
        return delta;
    }

    public void setDelta(Integer delta) {
        this.delta = delta;
    }
}

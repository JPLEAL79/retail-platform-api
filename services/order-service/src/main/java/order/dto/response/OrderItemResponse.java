package order.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class OrderItemResponse {

    @JsonProperty("productoId")
    private Long productId;
    @JsonProperty("cantidad")
    private Integer quantity;
    @JsonProperty("precioUnitario")
    private BigDecimal unitPrice;
    @JsonProperty("subtotal")
    private BigDecimal subtotal;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}

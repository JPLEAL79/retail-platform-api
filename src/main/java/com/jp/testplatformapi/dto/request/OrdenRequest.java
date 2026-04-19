package com.jp.testplatformapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public class OrdenRequest {

    // The request carries ids instead of nested entities to keep the API contract simple.
    @NotNull(message = "El clienteId es obligatorio.")
    @Positive(message = "El clienteId debe ser mayor que cero.")
    private Long clienteId;

    @NotNull(message = "El total es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor que cero.")
    @Digits(integer = 12, fraction = 0, message = "El total en CLP no debe tener decimales.")
    private BigDecimal total;

    @Valid
    @NotEmpty(message = "La orden debe tener al menos un detalle.")
    private List<DetalleOrdenRequest> detalles;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<DetalleOrdenRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrdenRequest> detalles) {
        this.detalles = detalles;
    }
}

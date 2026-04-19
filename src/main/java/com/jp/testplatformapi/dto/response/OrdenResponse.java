package com.jp.testplatformapi.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class OrdenResponse {

    private Long id;
    private Long clienteId;
    private BigDecimal total;
    // The response returns the order header plus its detail lines.
    private List<DetalleOrdenResponse> detalles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<DetalleOrdenResponse> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrdenResponse> detalles) {
        this.detalles = detalles;
    }
}

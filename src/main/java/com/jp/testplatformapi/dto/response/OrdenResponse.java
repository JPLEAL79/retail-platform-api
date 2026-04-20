package com.jp.testplatformapi.dto.response;

import com.jp.testplatformapi.entity.EstadoOrden;

import java.math.BigDecimal;
import java.util.List;

public class OrdenResponse {

    private Long id;
    private Long clienteId;
    private BigDecimal total;
    private EstadoOrden estado;
    // The response returns the order header plus its detail lines.
    private List<DetalleOrdenResponse> detalles;
    private DireccionEntregaResponse direccionEntrega;

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

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public List<DetalleOrdenResponse> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrdenResponse> detalles) {
        this.detalles = detalles;
    }

    public DireccionEntregaResponse getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(DireccionEntregaResponse direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }
}

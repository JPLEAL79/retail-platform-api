package com.jp.testplatformapi.mapper;

import com.jp.testplatformapi.dto.request.DetalleOrdenRequest;
import com.jp.testplatformapi.dto.request.DireccionEntregaRequest;
import com.jp.testplatformapi.dto.request.OrdenRequest;
import com.jp.testplatformapi.dto.response.DetalleOrdenResponse;
import com.jp.testplatformapi.dto.response.DireccionEntregaResponse;
import com.jp.testplatformapi.dto.response.OrdenResponse;
import com.jp.testplatformapi.entity.DetalleOrden;
import com.jp.testplatformapi.entity.DireccionEntrega;
import com.jp.testplatformapi.entity.Orden;

import java.util.List;

public final class OrdenMapper {

    private OrdenMapper() {
    }

    public static Orden toEntity(OrdenRequest request) {
        // The mapper turns the external API contract into the internal entity model.
        Orden orden = new Orden();
        orden.setClienteId(request.getClienteId());
        orden.setTotal(request.getTotal());
        orden.setEstado(request.getEstado());
        orden.setDetalles(request.getDetalles()
                .stream()
                .map(OrdenMapper::toDetalleEntity)
                .toList());
        orden.setDireccionEntrega(toDireccionEntity(request.getDireccionEntrega()));

        return orden;
    }

    public static OrdenResponse toResponse(Orden orden) {
        // The response is built from the entity so controllers do not expose JPA objects directly.
        OrdenResponse response = new OrdenResponse();
        response.setId(orden.getId());
        response.setClienteId(orden.getClienteId());
        response.setTotal(orden.getTotal());
        response.setEstado(orden.getEstado());

        if (orden.getDetalles() != null) {
            List<DetalleOrdenResponse> detalles = orden.getDetalles()
                    .stream()
                    .map(OrdenMapper::toDetalleResponse)
                    .toList();
            response.setDetalles(detalles);
        }

        response.setDireccionEntrega(toDireccionResponse(orden.getDireccionEntrega()));

        return response;
    }

    private static DetalleOrden toDetalleEntity(DetalleOrdenRequest request) {
        DetalleOrden detalle = new DetalleOrden();
        detalle.setProductoId(request.getProductoId());
        detalle.setCantidad(request.getCantidad());
        return detalle;
    }

    private static DetalleOrdenResponse toDetalleResponse(DetalleOrden detalle) {
        DetalleOrdenResponse response = new DetalleOrdenResponse();
        response.setProductoId(detalle.getProductoId());
        response.setCantidad(detalle.getCantidad());
        return response;
    }

    private static DireccionEntrega toDireccionEntity(DireccionEntregaRequest request) {
        if (request == null) {
            return null;
        }

        DireccionEntrega direccion = new DireccionEntrega();
        direccion.setRegion(request.getRegion());
        direccion.setComuna(request.getComuna());
        direccion.setDireccion(request.getDireccion());
        direccion.setNumero(request.getNumero());
        direccion.setNumeroDepto(request.getNumeroDepto());
        direccion.setReferencia(request.getReferencia());
        return direccion;
    }

    private static DireccionEntregaResponse toDireccionResponse(DireccionEntrega direccion) {
        if (direccion == null) {
            return null;
        }

        DireccionEntregaResponse response = new DireccionEntregaResponse();
        response.setRegion(direccion.getRegion());
        response.setComuna(direccion.getComuna());
        response.setDireccion(direccion.getDireccion());
        response.setNumero(direccion.getNumero());
        response.setNumeroDepto(direccion.getNumeroDepto());
        response.setReferencia(direccion.getReferencia());
        return response;
    }
}

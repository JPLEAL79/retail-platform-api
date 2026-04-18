package com.jp.testplatformapi.mapper;

import com.jp.testplatformapi.dto.request.DetalleOrdenRequest;
import com.jp.testplatformapi.dto.request.OrdenRequest;
import com.jp.testplatformapi.dto.response.DetalleOrdenResponse;
import com.jp.testplatformapi.dto.response.OrdenResponse;
import com.jp.testplatformapi.entity.DetalleOrden;
import com.jp.testplatformapi.entity.Orden;

import java.util.List;

public final class OrdenMapper {

    private OrdenMapper() {
    }

    public static Orden toEntity(OrdenRequest request) {
        Orden orden = new Orden();
        orden.setClienteId(request.getClienteId());
        orden.setTotal(request.getTotal());
        orden.setDetalles(request.getDetalles()
                .stream()
                .map(OrdenMapper::toDetalleEntity)
                .toList());

        return orden;
    }

    public static OrdenResponse toResponse(Orden orden) {
        OrdenResponse response = new OrdenResponse();
        response.setId(orden.getId());
        response.setClienteId(orden.getClienteId());
        response.setTotal(orden.getTotal());

        if (orden.getDetalles() != null) {
            List<DetalleOrdenResponse> detalles = orden.getDetalles()
                    .stream()
                    .map(OrdenMapper::toDetalleResponse)
                    .toList();
            response.setDetalles(detalles);
        }

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
}

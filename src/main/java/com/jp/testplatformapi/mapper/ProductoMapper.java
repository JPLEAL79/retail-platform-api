package com.jp.testplatformapi.mapper;

import com.jp.testplatformapi.dto.request.ProductoRequest;
import com.jp.testplatformapi.dto.response.ProductoResponse;
import com.jp.testplatformapi.entity.Producto;

public final class ProductoMapper {

    private ProductoMapper() {
    }

    public static Producto toEntity(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setSku(request.getSku());
        producto.setNombre(request.getNombre());
        producto.setMarca(request.getMarca());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setActivo(request.getActivo());
        return producto;
    }

    public static ProductoResponse toResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setSku(producto.getSku());
        response.setNombre(producto.getNombre());
        response.setMarca(producto.getMarca());
        response.setPrecio(producto.getPrecio());
        response.setStock(producto.getStock());
        response.setActivo(producto.getActivo());
        return response;
    }
}

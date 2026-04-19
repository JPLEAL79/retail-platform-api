package com.jp.testplatformapi.service;

import jakarta.annotation.Nonnull;
import com.jp.testplatformapi.entity.Producto;
import com.jp.testplatformapi.exception.ConflictException;
import com.jp.testplatformapi.exception.ResourceNotFoundException;
import com.jp.testplatformapi.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> getAll() {
        return repository.findAll();
    }

    public Producto getById(@Nonnull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id + "."));
    }

    public Producto getBySku(String sku) {
        return repository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con sku " + sku + "."));
    }

    public Producto create(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null.");
        }

        // SKU must stay unique because the API uses it as a business lookup.
        if (repository.existsBySku(producto.getSku())) {
            throw new ConflictException("Ya existe un producto con ese sku.");
        }

        return repository.save(producto);
    }

    public Producto update(@Nonnull Long id, Producto productoActualizado) {
        if (productoActualizado == null) {
            throw new IllegalArgumentException("El producto no puede ser null.");
        }

        Producto productoExistente = getById(id);

        // Same product can keep its SKU, but it cannot reuse one from another product.
        if (repository.existsBySkuAndIdNot(productoActualizado.getSku(), id)) {
            throw new ConflictException("Ya existe un producto con ese sku.");
        }

        // PUT replaces the current catalog state for this product.
        productoExistente.setSku(productoActualizado.getSku());
        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setMarca(productoActualizado.getMarca());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setStock(productoActualizado.getStock());
        productoExistente.setActivo(productoActualizado.getActivo());

        return repository.save(productoExistente);
    }
}

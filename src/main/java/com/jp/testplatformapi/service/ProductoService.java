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

        if (repository.existsBySku(producto.getSku())) {
            throw new ConflictException("Ya existe un producto con ese sku.");
        }

        return repository.save(producto);
    }
}

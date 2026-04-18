package com.jp.testplatformapi.service;

import jakarta.annotation.Nonnull;
import com.jp.testplatformapi.entity.DetalleOrden;
import com.jp.testplatformapi.entity.Orden;
import com.jp.testplatformapi.exception.ResourceNotFoundException;
import com.jp.testplatformapi.repository.OrdenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenService {

    private final OrdenRepository repository;

    public OrdenService(OrdenRepository repository) {
        this.repository = repository;
    }

    public Orden create(Orden orden) {
        if (orden == null) {
            throw new IllegalArgumentException("La orden no puede ser null.");
        }

        if (orden.getDetalles() == null || orden.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La orden debe contener al menos un detalle.");
        }

        // JPA needs both sides of the relationship to be synchronized before persisting.
        for (DetalleOrden detalle : orden.getDetalles()) {
            detalle.setOrden(orden);
        }

        return repository.save(orden);
    }

    public List<Orden> getAll() {
        return repository.findAll();
    }

    public Orden getById(@Nonnull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id " + id + "."));
    }
}

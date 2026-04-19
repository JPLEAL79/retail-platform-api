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

        // JPA needs both sides of the relationship in sync before saving the order.
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

    public Orden update(@Nonnull Long id, Orden ordenActualizada) {
        if (ordenActualizada == null) {
            throw new IllegalArgumentException("La orden no puede ser null.");
        }

        if (ordenActualizada.getDetalles() == null || ordenActualizada.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La orden debe contener al menos un detalle.");
        }

        Orden ordenExistente = getById(id);
        ordenExistente.setClienteId(ordenActualizada.getClienteId());
        ordenExistente.setTotal(ordenActualizada.getTotal());

        // PUT replaces the full order state, including its detail rows.
        ordenExistente.getDetalles().clear();

        for (DetalleOrden detalle : ordenActualizada.getDetalles()) {
            detalle.setOrden(ordenExistente);
            ordenExistente.getDetalles().add(detalle);
        }

        return repository.save(ordenExistente);
    }
}

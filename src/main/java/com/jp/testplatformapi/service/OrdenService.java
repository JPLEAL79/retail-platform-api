package com.jp.testplatformapi.service;

import com.jp.testplatformapi.entity.DetalleOrden;
import com.jp.testplatformapi.entity.Orden;
import com.jp.testplatformapi.entity.Producto;
import com.jp.testplatformapi.exception.ResourceNotFoundException;
import com.jp.testplatformapi.repository.ClienteRepository;
import com.jp.testplatformapi.repository.OrdenRepository;
import com.jp.testplatformapi.repository.ProductoRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdenService {

    private final OrdenRepository repository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public OrdenService(
            OrdenRepository repository,
            ClienteRepository clienteRepository,
            ProductoRepository productoRepository
    ) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    public Orden create(Orden orden) {
        validarOrden(orden);
        validarCliente(orden.getClienteId());
        prepararDetalles(orden, orden.getDetalles());
        orden.setTotal(calcularTotal(orden.getDetalles()));

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
        validarOrden(ordenActualizada);
        validarCliente(ordenActualizada.getClienteId());

        Orden ordenExistente = getById(id);
        ordenExistente.setClienteId(ordenActualizada.getClienteId());

        // PUT replaces the full order state, including its detail rows.
        ordenExistente.getDetalles().clear();
        prepararDetalles(ordenExistente, ordenActualizada.getDetalles());
        ordenExistente.setTotal(calcularTotal(ordenExistente.getDetalles()));

        return repository.save(ordenExistente);
    }

    private void validarOrden(Orden orden) {
        if (orden == null) {
            throw new IllegalArgumentException("La orden no puede ser null.");
        }

        if (orden.getDetalles() == null || orden.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La orden debe contener al menos un detalle.");
        }
    }

    private void validarCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id " + clienteId + ".");
        }
    }

    private void prepararDetalles(Orden orden, List<DetalleOrden> detallesEntrada) {
        // We validate each product id here so the order cannot reference missing catalog data.
        List<DetalleOrden> detallesPreparados = new ArrayList<>();

        for (DetalleOrden detalle : detallesEntrada) {
            obtenerProducto(detalle.getProductoId());
            detalle.setOrden(orden);
            detallesPreparados.add(detalle);
        }

        orden.setDetalles(detallesPreparados);
    }

    private BigDecimal calcularTotal(List<DetalleOrden> detalles) {
        BigDecimal total = BigDecimal.ZERO;

        for (DetalleOrden detalle : detalles) {
            Producto producto = obtenerProducto(detalle.getProductoId());
            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad()));
            total = total.add(subtotal);
        }

        return total;
    }

    private Producto obtenerProducto(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + productoId + "."));
    }
}

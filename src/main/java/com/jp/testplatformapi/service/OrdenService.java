package com.jp.testplatformapi.service;

import com.jp.testplatformapi.entity.DetalleOrden;
import com.jp.testplatformapi.entity.DireccionEntrega;
import com.jp.testplatformapi.entity.EstadoOrden;
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
        orden.setDetalles(construirDetalles(orden, orden.getDetalles()));
        asignarDireccionNueva(orden, orden.getDireccionEntrega());
        orden.setTotal(calcularTotal(orden.getDetalles()));
        orden.setEstado(resolverEstadoCreacion(orden.getEstado()));

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
        ordenExistente.setEstado(resolverEstadoActualizacion(ordenExistente.getEstado(), ordenActualizada.getEstado()));

        // PUT replaces the full order state, including its detail rows.
        ordenExistente.getDetalles().clear();
        ordenExistente.getDetalles().addAll(construirDetalles(ordenExistente, ordenActualizada.getDetalles()));
        actualizarDireccionEntrega(ordenExistente, ordenActualizada.getDireccionEntrega());
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

    private List<DetalleOrden> construirDetalles(Orden orden, List<DetalleOrden> detallesEntrada) {
        // We validate each product id here so the order cannot reference missing catalog data.
        List<DetalleOrden> detallesPreparados = new ArrayList<>();

        for (DetalleOrden detalle : detallesEntrada) {
            obtenerProducto(detalle.getProductoId());
            detalle.setOrden(orden);
            detallesPreparados.add(detalle);
        }

        return detallesPreparados;
    }

    private void asignarDireccionNueva(Orden orden, DireccionEntrega direccionEntrega) {
        if (direccionEntrega == null) {
            orden.setDireccionEntrega(null);
            return;
        }

        direccionEntrega.setOrden(orden);
        orden.setDireccionEntrega(direccionEntrega);
    }

    private void actualizarDireccionEntrega(Orden orden, DireccionEntrega direccionActualizada) {
        if (direccionActualizada == null) {
            orden.setDireccionEntrega(null);
            return;
        }

        if (orden.getDireccionEntrega() == null) {
            asignarDireccionNueva(orden, direccionActualizada);
            return;
        }

        DireccionEntrega direccionExistente = orden.getDireccionEntrega();
        direccionExistente.setRegion(direccionActualizada.getRegion());
        direccionExistente.setComuna(direccionActualizada.getComuna());
        direccionExistente.setDireccion(direccionActualizada.getDireccion());
        direccionExistente.setNumero(direccionActualizada.getNumero());
        direccionExistente.setNumeroDepto(direccionActualizada.getNumeroDepto());
        direccionExistente.setReferencia(direccionActualizada.getReferencia());
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

    private EstadoOrden resolverEstadoCreacion(EstadoOrden estado) {
        return estado != null ? estado : EstadoOrden.CREADA;
    }

    private EstadoOrden resolverEstadoActualizacion(EstadoOrden estadoActual, EstadoOrden estadoNuevo) {
        return estadoNuevo != null ? estadoNuevo : estadoActual;
    }
}

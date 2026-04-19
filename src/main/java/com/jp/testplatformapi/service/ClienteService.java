package com.jp.testplatformapi.service;

import jakarta.annotation.Nonnull;
import com.jp.testplatformapi.entity.Cliente;
import com.jp.testplatformapi.exception.ConflictException;
import com.jp.testplatformapi.exception.ResourceNotFoundException;
import com.jp.testplatformapi.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> getAll() {
        return repository.findAll();
    }

    public Cliente getById(@Nonnull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id + "."));
    }

    public Cliente getByRut(String rut) {
        return repository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con rut " + rut + "."));
    }

    public Cliente create(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser null.");
        }

        // We block duplicate RUT values before hitting the database constraint.
        if (repository.existsByRut(cliente.getRut())) {
            throw new ConflictException("Ya existe un cliente con ese rut.");
        }

        return repository.save(cliente);
    }

    public Cliente update(@Nonnull Long id, Cliente clienteActualizado) {
        if (clienteActualizado == null) {
            throw new IllegalArgumentException("El cliente no puede ser null.");
        }

        Cliente clienteExistente = getById(id);

        // Same customer can keep the same RUT, but it cannot collide with another record.
        if (repository.existsByRutAndIdNot(clienteActualizado.getRut(), id)) {
            throw new ConflictException("Ya existe un cliente con ese rut.");
        }

        // PUT is treated as the new full state of the customer.
        clienteExistente.setRut(clienteActualizado.getRut());
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setCorreo(clienteActualizado.getCorreo());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());

        return repository.save(clienteExistente);
    }
}

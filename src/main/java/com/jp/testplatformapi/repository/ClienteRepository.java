package com.jp.testplatformapi.repository;

import com.jp.testplatformapi.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Spring Data builds these queries from the method names.
    boolean existsByRut(String rut);

    boolean existsByRutAndIdNot(String rut, Long id);

    boolean existsByCorreo(String correo);

    boolean existsByCorreoAndIdNot(String correo, Long id);

    boolean existsByTelefono(String telefono);

    boolean existsByTelefonoAndIdNot(String telefono, Long id);

    Optional<Cliente> findByRut(String rut);
}

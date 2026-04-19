package com.jp.testplatformapi.repository;

import com.jp.testplatformapi.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Spring Data builds these queries from the method names.
    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);

    Optional<Producto> findBySku(String sku);
}

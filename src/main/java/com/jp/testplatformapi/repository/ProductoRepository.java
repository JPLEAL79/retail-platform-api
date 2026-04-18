package com.jp.testplatformapi.repository;

import com.jp.testplatformapi.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsBySku(String sku);

    Optional<Producto> findBySku(String sku);
}

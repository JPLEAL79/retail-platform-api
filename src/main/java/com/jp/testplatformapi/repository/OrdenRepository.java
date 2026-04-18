package com.jp.testplatformapi.repository;

import com.jp.testplatformapi.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
}

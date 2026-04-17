package com.jp.testplatformapi.repository;

import com.jp.testplatformapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
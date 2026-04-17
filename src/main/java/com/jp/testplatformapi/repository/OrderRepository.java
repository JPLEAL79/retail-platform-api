package com.jp.testplatformapi.repository;

import com.jp.testplatformapi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
package com.jp.testplatformapi.service;

import com.jp.testplatformapi.entity.Order;
import com.jp.testplatformapi.entity.OrderItem;
import com.jp.testplatformapi.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public Order create(Order order) {

        // Validación básica
        if (order == null) {
            throw new RuntimeException("Order cannot be null");
        }

        // asignar relación order → items
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            for (OrderItem item : order.getItems()) {
                item.setOrder(order);
            }
        }

        return repository.save(order);
    }

    public List<Order> getAll() {
        return repository.findAll();
    }
}
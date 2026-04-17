package com.jp.testplatformapi.controller;

import com.jp.testplatformapi.dto.OrderResponse;
import com.jp.testplatformapi.entity.Order;
import com.jp.testplatformapi.mapper.OrderMapper;
import com.jp.testplatformapi.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return service.create(order);
    }

    @GetMapping
    public List<OrderResponse> getAll() {
        return service.getAll()
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }
}
package com.jp.testplatformapi.mapper;

import com.jp.testplatformapi.dto.OrderItemResponse;
import com.jp.testplatformapi.dto.OrderResponse;
import com.jp.testplatformapi.entity.Order;
import com.jp.testplatformapi.entity.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponse toResponse(Order order) {

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotal(order.getTotal());

        if (order.getItems() != null) {
            List<OrderItemResponse> items = order.getItems()
                    .stream()
                    .map(OrderMapper::toItemResponse)
                    .collect(Collectors.toList());

            response.setItems(items);
        }

        return response;
    }

    private static OrderItemResponse toItemResponse(OrderItem item) {

        OrderItemResponse response = new OrderItemResponse();
        response.setProductId(item.getProductId());
        response.setQuantity(item.getQuantity());

        return response;
    }
}
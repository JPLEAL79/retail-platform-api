package com.jp.testplatformapi.dto;

import java.util.List;

public class OrderResponse {

    private Long id;
    private Long userId;
    private Double total;
    private List<OrderItemResponse> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public List<OrderItemResponse> getItems() { return items; }
    public void setItems(List<OrderItemResponse> items) { this.items = items; }
}
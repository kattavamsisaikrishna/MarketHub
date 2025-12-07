package com.vamsi.markethub.order;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    @Getter
    private Long id;
    @Getter
    private BigDecimal totalAmount;
    @Getter
    private String status;
    @Getter
    private LocalDateTime createdAt;
    @Getter
    private List<OrderItemResponse> items;

    public OrderResponse(){}

    public OrderResponse(Long id, BigDecimal totalAmount, String status, LocalDateTime createdAt, List<OrderItemResponse> items) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
    }
}

package com.vamsi.markethub.order;

import lombok.Getter;

public class OrderItemRequest {
    @Getter
    private Long productId;
    @Getter
    private Integer quantity;

    public OrderItemRequest(){}

    public OrderItemRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}

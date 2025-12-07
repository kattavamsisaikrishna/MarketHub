package com.vamsi.markethub.order;

import lombok.Getter;

import java.math.BigDecimal;

public class OrderItemResponse {

    @Getter
    private Long productId;
    @Getter
    private String productName;
    @Getter
    private Integer quantity;
    @Getter
    private BigDecimal priceAtPurchase;

    public OrderItemResponse(){}

    public OrderItemResponse(Long productId, String productName, Integer quantity, BigDecimal priceAtPurchase) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }
}

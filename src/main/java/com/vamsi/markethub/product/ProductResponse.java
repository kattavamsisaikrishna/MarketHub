package com.vamsi.markethub.product;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;
    private Long sellerId;
    private String sellerUsername;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, String description,
                           BigDecimal price, Integer stockQuantity,
                           String category, Long sellerId, String sellerUsername) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.sellerId = sellerId;
        this.sellerUsername = sellerUsername;
    }

}

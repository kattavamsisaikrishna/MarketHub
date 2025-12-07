package com.vamsi.markethub.product;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateProductRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;

    public CreateProductRequest(){}

    public CreateProductRequest(String name, String description, BigDecimal price, Integer stockQuantity, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
    }
}

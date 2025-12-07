package com.vamsi.markethub.product;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UpdateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;

    public UpdateProductRequest(){}
}

package com.vamsi.markethub.order;

import lombok.Getter;

import java.util.List;

public class CreateOrderRequest {

    @Getter
    List<OrderItemRequest>  items;

    public CreateOrderRequest(){}

    public CreateOrderRequest(List<OrderItemRequest> items) {
        this.items = items;
    }
}

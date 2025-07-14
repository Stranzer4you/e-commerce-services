package com.ecommerceservice.customers.model.request;


import lombok.Data;

@Data
public class CartItemRequestDTO {
    private Long customerId;
    private Long productId;
    private Integer quantity;
    private Double price;

}

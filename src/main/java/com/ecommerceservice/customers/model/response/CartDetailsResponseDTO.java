package com.ecommerceservice.customers.model.response;

import lombok.Data;

@Data
public class CartDetailsResponseDTO {
    private Long customerId;
    private Long productId;
    private Integer quantity;
}

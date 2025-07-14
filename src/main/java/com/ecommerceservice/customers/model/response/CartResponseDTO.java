package com.ecommerceservice.customers.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {
    private Long productId;
    private String productName;
    private Double price;
    private Double rating;
    private Integer quantity;
    private Long id;
}

package com.ecommerceservice.customers.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponseDTO {
    private Long productId;
    private String productName;
    private Double price;
    private Double rating;
}

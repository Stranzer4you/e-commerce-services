package com.ecommerceservice.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String productName;
    private Double price;
    private Integer quantity;
    private Boolean isAvailable;
}

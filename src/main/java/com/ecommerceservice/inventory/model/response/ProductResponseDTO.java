package com.ecommerceservice.inventory.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String productName;
    private Double price;
    private Integer quantity;
    private Boolean isAvailable;
    private Integer productCategoryId;
    private Double rating;
    private Boolean isWishListed;
    private Boolean isInCart;
}

package com.ecommerceservice.inventory.model.request;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class AddProductRequestDTO {
    @NotNull(message = "productName shouldn't be empty")
    private String productName;
    @NotNull(message = "price shouldn't be empty")
    @Positive(message = "price must be greater than 0")
    private Double price;
    @Positive(message = "quantity must be greater than 0")
    @NotNull(message = "quantity shouldn't be empty")
    private Integer quantity;
    private Boolean IsAvailable=true;
}

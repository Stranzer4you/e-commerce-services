package com.ecommerceservice.inventory.model.request;


import lombok.Data;

import com.ecommerceservice.utility.constants.ExceptionConstants;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class AddProductRequestDTO {
    @NotNull(message = ExceptionConstants.PRODUCT_NAME_SHOULD_NOT_BE_EMPTY)
    private String productName;
    @NotNull(message = ExceptionConstants.PRODUCT_PRICE_SHOULD_NOT_BE_EMPTY)
    @Positive(message = ExceptionConstants.PRODUCT_PRICE_GREATER_ZERO)
    private Double price;
    @Positive(message = ExceptionConstants.QUALITY_GREATER_ZERO)
    @NotNull(message = ExceptionConstants.QUALITY_SHOULD_NOT_BE_EMPTY)
    private Integer quantity;
    private Boolean IsAvailable=true;
}

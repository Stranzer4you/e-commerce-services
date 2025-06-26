package com.ecommerceservice.inventory.model.request;

import com.ecommerceservice.utility.constants.ExceptionConstants;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class UpdateProductRequestDto {
    @NotNull(message = ExceptionConstants.PRODUCT_NAME_SHOULD_NOT_BE_EMPTY)
    private String productName;
    @NotNull(message = ExceptionConstants.PRODUCT_PRICE_SHOULD_NOT_BE_EMPTY)
    @Positive(message = ExceptionConstants.PRODUCT_PRICE_GREATER_ZERO)
    private Double price;
    @Positive(message = ExceptionConstants.QUALITY_GREATER_ZERO)
    @NotNull(message = ExceptionConstants.QUALITY_SHOULD_NOT_BE_EMPTY)
    private Integer quantity;
    @NotNull(message = ExceptionConstants.IS_AVAILABLE_SHOULD_NOT_BE_NULL)
    private Boolean IsAvailable;
    @NotNull(message = ExceptionConstants.IS_ACTIVE_SHOULD_NOT_BE_NULL)
    private Boolean IsActive;
}

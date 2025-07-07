package com.ecommerceservice.customers.model.request;

import com.ecommerceservice.utility.constants.ExceptionConstants;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WishlistItemRequestDTO {
    @NotNull(message = ExceptionConstants.CUSTOMER_ID_SHOULD_NOT_BE_EMPTY)
    private Long customerId;
    @NotNull(message = ExceptionConstants.PRODUCT_ID_SHOULD_NOT_BE_EMPTY)
    private Long productId;
}

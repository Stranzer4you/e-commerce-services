package com.ecommerceservice.orders.model.request;

import com.ecommerceservice.utility.constants.ExceptionConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDetailRequestDto {
    @NotNull(message = ExceptionConstants.PRODUCT_ID_SHOULD_NOT_BE_EMPTY)
    private Long productId;
    @NotNull(message = ExceptionConstants.QUANTITY_SHOULD_NOT_BE_EMPTY)
    private Integer quantity;
    @NotNull(message =  ExceptionConstants.AMOUNT_PAID_SHOULD_NOT_BE_EMPTY)
    private Double amountPaid;
}

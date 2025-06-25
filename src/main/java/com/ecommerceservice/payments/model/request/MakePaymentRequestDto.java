package com.ecommerceservice.payments.model.request;

import com.ecommerceservice.utility.constants.ExceptionConstants;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MakePaymentRequestDto {
    @NotNull(message = ExceptionConstants.ORDER_ID_SHOULD_NOT_BE_EMPTY)
    private Long orderId;
    @NotNull(message = ExceptionConstants.CUSTOMER_ID_SHOULD_NOT_BE_EMPTY)
    private Long customerId;
    @NotNull(message = ExceptionConstants.AMOUNT_PAID_SHOULD_NOT_BE_EMPTY)
    private Double amount;
}

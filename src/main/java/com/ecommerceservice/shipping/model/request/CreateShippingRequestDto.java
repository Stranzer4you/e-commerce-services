package com.ecommerceservice.shipping.model.request;

import com.ecommerceservice.utility.ExceptionConstants;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateShippingRequestDto {
    @NotNull(message = ExceptionConstants.ORDER_ID_SHOULD_NOT_BE_EMPTY)
    private Long orderId;
    @NotNull(message = ExceptionConstants.CUSTOMER_ID_SHOULD_NOT_BE_EMPTY)
    private Long customerId;
    @NotNull(message = ExceptionConstants.ADDRESS_SHOULD_NOT_EMPTY)
    private String address;
    @NotNull(message = ExceptionConstants.STATUS_SHOULD_NOT_BE_EMPTY)
    private Integer status;
}

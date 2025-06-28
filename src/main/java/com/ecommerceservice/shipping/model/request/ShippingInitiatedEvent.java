package com.ecommerceservice.shipping.model.request;

import com.ecommerceservice.utility.constants.ExceptionConstants;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ShippingInitiatedEvent {
    @NotNull(message = ExceptionConstants.ORDER_ID_SHOULD_NOT_BE_EMPTY)
    private Long orderId;
    @NotNull(message = ExceptionConstants.CUSTOMER_ID_SHOULD_NOT_BE_EMPTY)
    private Long customerId;
}

package com.ecommerceservice.orders.model.request;


import com.ecommerceservice.utility.CommonConstants;
import com.ecommerceservice.utility.ExceptionConstants;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
public class CreateOrderRequestDto {
    @NotNull(message = ExceptionConstants.CUSTOMER_ID_SHOULD_NOT_BE_EMPTY)
    private Long customerId;
    @NotNull(message = ExceptionConstants.TOTAL_AMOUNT_SHOULD_NOT_BE_EMPTY)
    @Positive(message = ExceptionConstants.TOTAL_AMOUNT_GREATER_THAN_ZERO)
    private Double totalAmount;
    @NotNull(message=ExceptionConstants.STATUS_SHOULD_NOT_BE_EMPTY)
    private Integer status;
    @Valid
    @NotEmpty(message = ExceptionConstants.ORDERS_DETAILS_SHOULD_NOT_BE_EMPTY)
    private List<OrdersDetailRequestDto> ordersDetails;

}

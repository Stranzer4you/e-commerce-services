package com.ecommerceservice.payments.model.request;

import lombok.Data;

@Data
public class UpdateOrderStatusDto {
    private Long orderId;
    private Integer status;
}

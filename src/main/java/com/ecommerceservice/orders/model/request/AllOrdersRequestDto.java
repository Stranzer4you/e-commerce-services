package com.ecommerceservice.orders.model.request;

import lombok.Data;

import java.util.List;

@Data
public class AllOrdersRequestDto {
    private Integer orderStatus;
    private Long customerId;
}

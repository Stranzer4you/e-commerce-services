package com.ecommerceservice.shipping.model.request;

import lombok.Data;

import java.util.List;

@Data
public class ShippingRequestDto {
    private List<Integer> shippingStatus;
}

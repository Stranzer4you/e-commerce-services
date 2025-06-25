package com.ecommerceservice.utility.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ShippingStatusEnum {
    SHIPPING(6,"Shipping"),
    SHIPPED(7,"Shipped"),
    DELIVERED(8,"Delivered");

    public final Integer statusId;
    public final String status;

    public static ShippingStatusEnum fromStatusId(Integer statusId){
        for( ShippingStatusEnum statusEnum : values()){
            if(Objects.equals(statusEnum.getStatusId(),statusId)){
                return  statusEnum;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + statusId);
    }
}

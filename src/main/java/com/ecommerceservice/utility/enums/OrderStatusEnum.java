package com.ecommerceservice.utility.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    PROCESSING(1,"Processing"),
    PROCESSED(2,"Processed"),
    FAILED(3,"Failed");

    public final Integer statusId;
    public final String status;


    public static OrderStatusEnum fromStatusId(Integer statusId){
        for( OrderStatusEnum statusEnum : values()){
            if(Objects.equals(statusEnum.getStatusId(),statusId)){
                return  statusEnum;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + statusId);
    }
}

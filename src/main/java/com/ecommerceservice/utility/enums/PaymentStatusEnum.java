package com.ecommerceservice.utility.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.Random;

@Getter
@AllArgsConstructor
public enum PaymentStatusEnum {
    SUCCESS(5,"Success"),
    FAILED(3,"Failed"),
    PENDING(4,"Pending");

    private final Integer statusId;
    private final String status;

    public static PaymentStatusEnum simulate() {
        int rand = new Random().nextInt(3);
        return values()[rand];
    }

    public static PaymentStatusEnum fromStatusId(Integer statusId) {
        for (PaymentStatusEnum status : values()) {
            if (Objects.equals(status.getStatusId(), statusId)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + statusId);
    }

}


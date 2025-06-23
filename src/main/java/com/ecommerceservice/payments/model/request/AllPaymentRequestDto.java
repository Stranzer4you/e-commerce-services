package com.ecommerceservice.payments.model.request;

import lombok.Data;

import java.util.List;

@Data
public class AllPaymentRequestDto {
    private List<Integer> paymentStatus;
}

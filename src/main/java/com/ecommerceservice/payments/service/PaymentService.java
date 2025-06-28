package com.ecommerceservice.payments.service;

import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.payments.model.request.AllPaymentRequestDto;
import com.ecommerceservice.payments.model.request.PaymentInitiatedEvent;

public interface PaymentService {

    BaseResponse getAllPayments(AllPaymentRequestDto dto);

    BaseResponse makePayment(PaymentInitiatedEvent dto) throws BadRequestException;
}

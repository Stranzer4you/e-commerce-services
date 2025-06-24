package com.ecommerceservice.payments.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.payments.model.request.AllPaymentRequestDto;
import com.ecommerceservice.payments.model.request.MakePaymentRequestDto;

public interface PaymentService {

    BaseResponse getAllPayments(AllPaymentRequestDto dto);

    BaseResponse makePayment(MakePaymentRequestDto dto) throws BadRequestException;
}

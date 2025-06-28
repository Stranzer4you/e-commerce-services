package com.ecommerceservice.payments.controller;


import com.ecommerceservice.payments.model.request.PaymentInitiatedEvent;
import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.payments.model.request.AllPaymentRequestDto;
import com.ecommerceservice.payments.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping()
    public BaseResponse getAllPayments(@Valid @ModelAttribute AllPaymentRequestDto dto) {
        return paymentService.getAllPayments(dto);
    }

    @PostMapping()
    public BaseResponse makePayment(@Valid @RequestBody PaymentInitiatedEvent dto) throws BadRequestException {
        return paymentService.makePayment(dto);
    }

}


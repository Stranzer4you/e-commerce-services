package com.ecommerceservice.payments.kafka;


import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.payments.model.request.PaymentInitiatedEvent;
import com.ecommerceservice.payments.service.PaymentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentConsumer {

    @Autowired
    private PaymentServiceImpl paymentService;

    @KafkaListener(topics = "#{'${kafka.order.payment.initiated.topic}'}",groupId = "payment-service-group",concurrency = "3")
    public void handlePaymentInitiatedEvent(PaymentInitiatedEvent event) throws BadRequestException {
        log.info("Received Payment Initiated Event -> {}",event);
        paymentService.makePayment(event);
    }
}

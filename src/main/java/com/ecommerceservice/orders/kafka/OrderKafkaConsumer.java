package com.ecommerceservice.orders.kafka;


import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.payments.model.request.UpdateOrderStatusDto;
import com.ecommerceservice.utility.MasterUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderKafkaConsumer {


    @Autowired
    private MasterUtility masterUtility;

    @KafkaListener(topics ="#{'${kafka.payment.success.topic}'}" , groupId = "order-service-group", concurrency = "3")
    public void handleUpdateOrderSuccessStatus(UpdateOrderStatusDto event) throws BadRequestException {
        log.info("Received payment SUCCESS event: {}", event);
        masterUtility.updateOrderStatusAndNotify(event);
    }

    @KafkaListener(topics ="#{'${kafka.payment.failed.topic}'}" , groupId = "order-service-group", concurrency = "3")
    public void handleUpdateOrderFailedStatus(UpdateOrderStatusDto event) throws BadRequestException {
        log.info("Received payment failed event: {}", event);
        masterUtility.updateOrderStatusAndNotify(event);
    }

}

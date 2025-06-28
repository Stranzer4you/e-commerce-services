package com.ecommerceservice.shipping.kafka;


import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.shipping.model.request.ShippingInitiatedEvent;
import com.ecommerceservice.shipping.service.ShippingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShippingConsumer {

    @Autowired
    private ShippingServiceImpl shippingService;

    @KafkaListener(topics = "#{'${kafka.shipping.initiate.topic}'}",groupId = "shipping-service-group",concurrency = "3")
    public void handleShippingInitiatedEvent(ShippingInitiatedEvent event) throws BadRequestException {
        shippingService.createShipping(event);
    }

}

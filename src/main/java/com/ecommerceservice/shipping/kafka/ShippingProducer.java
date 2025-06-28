package com.ecommerceservice.shipping.kafka;

import com.ecommerceservice.notifications.model.request.NotificationRequestEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class ShippingProducer {

    @Value("${kafka.shipping.status.topic}")
    private String shippingStatusTopic;

    @Autowired
    private KafkaTemplate<String , NotificationRequestEvent> kafkaShippingTemplate;

    public void publishToShippingStatusTopic(NotificationRequestEvent event){
        Message<NotificationRequestEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC,shippingStatusTopic).setHeader(KafkaHeaders.MESSAGE_KEY,String.valueOf(event.getOrderId())).build();
        kafkaShippingTemplate.send(message);
    }

}

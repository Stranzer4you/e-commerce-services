package com.ecommerceservice.payments.kafka;

import com.ecommerceservice.notifications.model.request.NotificationRequestEvent;
import com.ecommerceservice.orders.model.request.InventoryUpdateEvent;
import com.ecommerceservice.shipping.model.request.ShippingInitiatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    @Value("${kafka.payment.success.topic}")
    private String paymentSuccessTopic;

    @Value("${kafka.payment.failed.topic}")
    private String paymentFailedTopic;

    @Value("${kafka.payment.pending.topic}")
    private String paymentPendingTopic;

    @Value("${kafka.inventory.update.topic}")
    private String inventoryUpdateTopic;

    @Value("${kafka.shipping.initiate.topic}")
    private String shippingInitiatedTopic;


    @Autowired
    private KafkaTemplate<String, NotificationRequestEvent> kafkaPaymentTemplate;

    @Autowired
    private KafkaTemplate<String, InventoryUpdateEvent> kafkaInventoryTemplate;

    @Autowired
    private KafkaTemplate<String, ShippingInitiatedEvent> kafkaShippingTemplate;

    public void publishToPaymentSuccessTopic(NotificationRequestEvent event){
        Message<NotificationRequestEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC,paymentSuccessTopic).setHeader(KafkaHeaders.MESSAGE_KEY,String.valueOf(event.getOrderId())).build();
        kafkaPaymentTemplate.send(message);
    }

    public void publishToPaymentFailedTopic(NotificationRequestEvent event){
        Message<NotificationRequestEvent> message  = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC,paymentFailedTopic).setHeader(KafkaHeaders.MESSAGE_KEY,String.valueOf(event.getOrderId())).build();
        kafkaPaymentTemplate.send(message);
    }

    public void publishToPaymentPendingTopic(NotificationRequestEvent event){
        Message<NotificationRequestEvent> message  = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC,paymentPendingTopic).setHeader(KafkaHeaders.MESSAGE_KEY,String.valueOf(event.getOrderId())).build();
        kafkaPaymentTemplate.send(message);
    }

    public void publishToInventoryUpdateTopic(InventoryUpdateEvent event){
        Message<InventoryUpdateEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC,inventoryUpdateTopic).setHeader(KafkaHeaders.MESSAGE_KEY,String.valueOf(event.getOrdersDetailsDaoList().get(0).getOrderId())).build();
        kafkaInventoryTemplate.send(message);

    }

    public void publishToShippingInitiateTopic(ShippingInitiatedEvent event){
        Message<ShippingInitiatedEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC,shippingInitiatedTopic).setHeader(KafkaHeaders.MESSAGE_KEY,String.valueOf(event.getOrderId())).build();
        kafkaShippingTemplate.send(message);
    }


}

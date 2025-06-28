package com.ecommerceservice.orders.kafka;


import com.ecommerceservice.notifications.model.request.NotificationRequestEvent;
import com.ecommerceservice.payments.model.request.PaymentInitiatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrderKafkaProducer {

    @Value("${kafka.orders.created.topic}")
    private String orderCreatedTopic;

    @Value("${kafka.order.payment.initiated.topic}")
    private String paymentInitiatedTopic;

    @Value("${kafka.order.status.updated.topic}")
    private String orderStatusUpdateTopic;


    @Autowired
    private KafkaTemplate<String, NotificationRequestEvent> kafkaOrderCreateTemplate;

    @Autowired
    private KafkaTemplate<String, PaymentInitiatedEvent> kafkaPaymentInitiatedTemplate;

    @Autowired
    private KafkaTemplate<String,NotificationRequestEvent> kafkaOrderStatusUpdateTemplate;


    public void publishToOrderCreatedTopic(NotificationRequestEvent event) {
        Message<NotificationRequestEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC,orderCreatedTopic).setHeader(KafkaHeaders.MESSAGE_KEY,String.valueOf(event.getOrderId())).build();
        kafkaOrderCreateTemplate.send(message);
    }

    public void publishToPaymentInitiation(PaymentInitiatedEvent event) {
        Message<PaymentInitiatedEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC,paymentInitiatedTopic).setHeader(KafkaHeaders.MESSAGE_KEY,String.valueOf(event.getOrderId())).build();
        kafkaPaymentInitiatedTemplate.send(message);
    }

    public void publishToStatusUpdate(NotificationRequestEvent event) {
        Message<NotificationRequestEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC,orderStatusUpdateTopic).setHeader(KafkaHeaders.MESSAGE_KEY,String.valueOf(event.getOrderId())).build();
        kafkaOrderStatusUpdateTemplate.send(message);
    }
}

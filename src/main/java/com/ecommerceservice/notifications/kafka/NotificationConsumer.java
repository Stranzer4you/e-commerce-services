package com.ecommerceservice.notifications.kafka;

import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.notifications.model.request.NotificationRequestEvent;
import com.ecommerceservice.notifications.service.NotificationServiceImpl;
import com.ecommerceservice.payments.model.request.PaymentInitiatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {

    @Autowired
    private NotificationServiceImpl notificationService;

    @Autowired
    private KafkaTemplate<String, NotificationRequestEvent>  kafkaNotificationTemplate;



    @KafkaListener(topics = "#{'${kafka.orders.created.topic}'}",groupId = "notification-service-group",concurrency = "3")
    public void handleOrderCreatedEvent(NotificationRequestEvent event) throws BadRequestException {
        log.info("Received Order created  Event -> {}",event);
        notificationService.sendSmsEmailPushNotifications(event);
    }

    @KafkaListener(topics = "#{'${kafka.order.status.updated.topic}'}",groupId = "notification-service-group",concurrency = "3")
    public void handleOrderStatusUpdateEvent(NotificationRequestEvent event) throws BadRequestException {
        log.info("Received Order Status Update  Event -> {}",event);
        notificationService.sendSmsEmailPushNotifications(event);
    }

    @KafkaListener(topics = "#{'${kafka.payment.success.topic}'}",groupId = "notification-service-group",concurrency = "3")
    public void handlePaymentSuccessEvent(NotificationRequestEvent event) throws BadRequestException {
        log.info("Received Payment Success  Event -> {}",event);
        notificationService.sendSmsEmailPushNotifications(event);
    }

    @KafkaListener(topics = "#{'${kafka.payment.pending.topic}'}",groupId = "notification-service-group",concurrency = "3")
    public void handlePaymentPendingEvent(NotificationRequestEvent event) throws BadRequestException {
        log.info("Received Payment pending  Event -> {}",event);
        notificationService.sendSmsEmailPushNotifications(event);
    }

    @KafkaListener(topics = "#{'${kafka.payment.failed.topic}'}",groupId = "notification-service-group",concurrency = "3")
    public void handlePaymentFailedEvent(NotificationRequestEvent event) throws BadRequestException {
        log.info("Received Payment Failed  Event -> {}",event);
        notificationService.sendSmsEmailPushNotifications(event);
    }

    @KafkaListener(topics = "#{'${kafka.shipping.status.topic}'}",groupId = "notification-service-group",concurrency = "3")
    public void handleShippingStatusEvent(NotificationRequestEvent event) throws BadRequestException {
        log.info("Received Shipping Status  Event -> {}",event);
        notificationService.sendSmsEmailPushNotifications(event);
    }

}

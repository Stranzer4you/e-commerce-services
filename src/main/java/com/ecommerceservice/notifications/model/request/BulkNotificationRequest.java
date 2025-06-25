package com.ecommerceservice.notifications.model.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BulkNotificationRequest {
    private Long customerId;
    private Long orderId;
    private Integer status;
    private String message;
    private Integer notificationModuleId;
    private Double amount;
    private Long productId;
}

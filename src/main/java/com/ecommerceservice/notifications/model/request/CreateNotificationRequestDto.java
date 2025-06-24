package com.ecommerceservice.notifications.model.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateNotificationRequestDto {
    private Long customerId;
    private Long orderId;
    private Integer notificationType;
    private Integer status;
    private String message;
    private String error;
    private LocalDateTime createdAt;
    private Integer notificationModuleId;
}


package com.ecommerceservice.notifications.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private Long moduleId;
    private String module;
    private Long notificationTypeId;
    private String notificationType;
    private String message;
    private String status;
    private Long notificationId;
}

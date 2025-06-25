package com.ecommerceservice.notifications.model.request;

import lombok.Data;

import java.util.List;

@Data
public class BulkNotificationRequest {
    private Long customerId;
    private Long orderId;
    private Integer status;
    private Integer notificationModuleId;
    private Double amount;
    private List<Long> productIds;
}

package com.ecommerceservice.notifications.model.request;

import lombok.Data;

import java.util.List;

@Data
public class AllNotificationsRequestDto {
    private List<Integer> notificationStatus;
}

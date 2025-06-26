package com.ecommerceservice.notifications.service;

import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.notifications.model.request.AllNotificationsRequestDto;
import com.ecommerceservice.notifications.model.request.CreateNotificationRequestDto;

import java.util.List;

public interface NotificationService {

    BaseResponse getAllNotifications(AllNotificationsRequestDto dto);

    BaseResponse createNotification(CreateNotificationRequestDto dto);

    BaseResponse createBulkNotifications(List<CreateNotificationRequestDto> dtoList);
}

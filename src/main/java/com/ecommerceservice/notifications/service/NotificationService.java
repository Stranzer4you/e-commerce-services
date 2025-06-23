package com.ecommerceservice.notifications.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.notifications.model.request.AllNotificationsRequestDto;
import com.ecommerceservice.notifications.model.request.CreateNotificationRequestDto;

public interface NotificationService {

    BaseResponse getAllNotifications(AllNotificationsRequestDto dto);

    BaseResponse createNotification(CreateNotificationRequestDto dto);
}

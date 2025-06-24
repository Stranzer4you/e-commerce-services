package com.ecommerceservice.notifications.controller;


import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.notifications.model.request.AllNotificationsRequestDto;
import com.ecommerceservice.notifications.model.request.CreateNotificationRequestDto;
import com.ecommerceservice.notifications.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @GetMapping()
    public BaseResponse getAllOrders(@ModelAttribute AllNotificationsRequestDto dto){
        return notificationService.getAllNotifications(dto);
    }

    @PostMapping()
    public BaseResponse createNotification(@RequestBody CreateNotificationRequestDto dto){
        return notificationService.createNotification(dto);
    }

    @PostMapping("/bulk")
    public BaseResponse createdBulkNotifications(@RequestBody List<CreateNotificationRequestDto> dtoList){
        return  notificationService.createBulkNotifications(dtoList);

    }

}


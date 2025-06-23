package com.ecommerceservice.notifications.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.config.BaseResponseUtility;
import com.ecommerceservice.customers.dao.CustomerDao;
import com.ecommerceservice.customers.repository.CustomerRepository;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.inventory.dao.Products;
import com.ecommerceservice.inventory.repository.InventoryRepository;
import com.ecommerceservice.notifications.dao.NotificationDao;
import com.ecommerceservice.notifications.mapper.NotificationMapper;
import com.ecommerceservice.notifications.model.request.AllNotificationsRequestDto;
import com.ecommerceservice.notifications.model.request.CreateNotificationRequestDto;
import com.ecommerceservice.notifications.repository.NotificationRepository;
import com.ecommerceservice.orders.dao.OrdersDao;
import com.ecommerceservice.utility.CommonConstants;
import com.ecommerceservice.utility.ExceptionConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {


    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationRepository notificationRepository;


    @Override
    public BaseResponse getAllNotifications(AllNotificationsRequestDto dto) {
        List<NotificationDao> notificationDaos;
        if(!CollectionUtils.isEmpty(dto.getNotificationStatus())){
            notificationDaos = notificationRepository.findAllByStatusIn(dto.getNotificationStatus());
        }
        else{
            notificationDaos = notificationRepository.findAll();
        }
        return BaseResponseUtility.getBaseResponse(notificationDaos);
    }

    @Override
    public BaseResponse createNotification(CreateNotificationRequestDto dto) {
        NotificationDao notificationDao = notificationMapper.notificationDtoToDao(dto);
        notificationDao.setCreatedAt(LocalDateTime.now());
        notificationDao = notificationRepository.save(notificationDao);
        return BaseResponseUtility.getBaseResponse(notificationDao);
    }
}

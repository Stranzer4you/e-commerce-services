package com.ecommerceservice.notifications.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.config.BaseResponseUtility;
import com.ecommerceservice.notifications.dao.NotificationDao;
import com.ecommerceservice.notifications.mapper.NotificationMapper;
import com.ecommerceservice.notifications.model.request.AllNotificationsRequestDto;
import com.ecommerceservice.notifications.model.request.CreateNotificationRequestDto;
import com.ecommerceservice.notifications.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;


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
        notificationDao.setNotifyTime(LocalDateTime.now());
        notificationDao = notificationRepository.save(notificationDao);
        return BaseResponseUtility.getBaseResponse(notificationDao);
    }

    @Override
    public BaseResponse createBulkNotifications(List<CreateNotificationRequestDto> dtoList) {
        List<NotificationDao> notificationDaos = notificationMapper.notificationDtoListToDaoList(dtoList);
        notificationDaos.forEach(x->x.setNotifyTime(LocalDateTime.now()));
        notificationDaos = notificationRepository.saveAll(notificationDaos);
        return BaseResponseUtility.getBaseResponse(notificationDaos);
    }
}

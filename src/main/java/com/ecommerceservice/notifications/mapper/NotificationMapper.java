package com.ecommerceservice.notifications.mapper;

import com.ecommerceservice.notifications.dao.NotificationDao;
import com.ecommerceservice.notifications.model.request.CreateNotificationRequestDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDao notificationDtoToDao(CreateNotificationRequestDto dto);

    List<NotificationDao> notificationDtoListToDaoList(List<CreateNotificationRequestDto> dtoList);
}

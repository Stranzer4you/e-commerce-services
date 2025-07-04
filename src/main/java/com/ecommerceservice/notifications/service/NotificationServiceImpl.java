package com.ecommerceservice.notifications.service;

import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.utility.BaseResponseUtility;
import com.ecommerceservice.customers.dao.CustomerDao;
import com.ecommerceservice.customers.repository.CustomerRepository;
import com.ecommerceservice.inventory.dao.Product;
import com.ecommerceservice.inventory.repository.InventoryRepository;
import com.ecommerceservice.notifications.dao.NotificationDao;
import com.ecommerceservice.notifications.mapper.NotificationMapper;
import com.ecommerceservice.notifications.model.request.AllNotificationsRequestDto;
import com.ecommerceservice.notifications.model.request.NotificationRequestEvent;
import com.ecommerceservice.notifications.model.request.CreateNotificationRequestDto;
import com.ecommerceservice.notifications.model.request.TemplatePlaceHoldersDto;
import com.ecommerceservice.notifications.repository.NotificationRepository;
import com.ecommerceservice.utility.enums.ModuleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private NotificationMessageServiceImpl notificationMessageService;

    @Value("${notification.type.ids}")
    private List<Integer> notificationTypeIds;


    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse createNotification(CreateNotificationRequestDto dto) {
        NotificationDao notificationDao = notificationMapper.notificationDtoToDao(dto);
        notificationDao.setNotifyTime(LocalDateTime.now());
        notificationDao = notificationRepository.save(notificationDao);
        return BaseResponseUtility.getBaseResponse(notificationDao);
    }


    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse createBulkNotifications(List<CreateNotificationRequestDto> dtoList) {
        List<NotificationDao> notificationDaos = notificationMapper.notificationDtoListToDaoList(dtoList);
        notificationDaos.forEach(x->x.setNotifyTime(LocalDateTime.now()));
        notificationDaos = notificationRepository.saveAll(notificationDaos);
        return BaseResponseUtility.getBaseResponse(notificationDaos);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void sendSmsEmailPushNotifications(NotificationRequestEvent dto){
        List<CreateNotificationRequestDto> notifications = new ArrayList<>();
        notificationTypeIds.forEach(type->{
            CreateNotificationRequestDto createNotificationRequestDto = new CreateNotificationRequestDto();
            createNotificationRequestDto.setOrderId(dto.getOrderId());
            createNotificationRequestDto.setStatus(dto.getStatus());
            createNotificationRequestDto.setCustomerId(dto.getCustomerId());
            TemplatePlaceHoldersDto templatePlaceHoldersDto = generatePlaceHoldersDto(dto.getCustomerId(),dto.getProductIds());
            if(dto.getNotificationModuleId().equals(ModuleEnum.PAYMENTS.getModuleId())){
                templatePlaceHoldersDto.setAmount(dto.getAmount());
            }
            createNotificationRequestDto.setMessage(notificationMessageService.generateMessage(dto.getNotificationModuleId(), dto.getStatus(),type,templatePlaceHoldersDto));
            createNotificationRequestDto.setNotificationModuleId(dto.getNotificationModuleId());
            createNotificationRequestDto.setNotifyTime(LocalDateTime.now());
            createNotificationRequestDto.setNotificationType(type);
            notifications.add(createNotificationRequestDto);
        });
        if(!CollectionUtils.isEmpty(notifications)){
            this.createBulkNotifications(notifications);
        }
    }

    public TemplatePlaceHoldersDto generatePlaceHoldersDto(Long customerId,List<Long> productIds){
        CustomerDao customerDao = customerRepository.findByIdAndIsActiveTrue(customerId);
        List<Product> products  = inventoryRepository.findAllByIdInAndIsActiveTrue(productIds);
        String productsNames = products.stream().map(Product::getProductName).collect(Collectors.joining(","));
        if(products.size() >3){
            productsNames =  products.subList(0,3).stream().map(Product::getProductName).collect(Collectors.joining(", ")) + " + others ";
        }
        TemplatePlaceHoldersDto dto=new TemplatePlaceHoldersDto();
        dto.setProductName(productsNames);
        dto.setCustomerName(customerDao.getCustomerName());
        return dto;
    }
}

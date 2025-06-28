package com.ecommerceservice.shipping.service;

import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.utility.BaseResponseUtility;
import com.ecommerceservice.customers.dao.CustomerDao;
import com.ecommerceservice.customers.repository.CustomerRepository;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.notifications.model.request.NotificationRequestEvent;
import com.ecommerceservice.notifications.service.NotificationServiceImpl;
import com.ecommerceservice.orders.dao.OrdersDao;
import com.ecommerceservice.orders.dao.OrdersDetailsDao;
import com.ecommerceservice.orders.repository.OrdersRepository;
import com.ecommerceservice.shipping.dao.ShippingDao;
import com.ecommerceservice.shipping.mapper.ShippingMapper;
import com.ecommerceservice.shipping.model.request.ShippingRequestDto;
import com.ecommerceservice.shipping.model.request.ShippingInitiatedEvent;
import com.ecommerceservice.shipping.repository.ShippingRepository;
import com.ecommerceservice.utility.constants.ExceptionConstants;
import com.ecommerceservice.utility.enums.ModuleEnum;
import com.ecommerceservice.utility.enums.ShippingStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class ShippingServiceImpl implements ShippingService {


    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private NotificationServiceImpl notificationService;


    @Override
    public BaseResponse getAllShippings(ShippingRequestDto dto) {
        List<ShippingDao> shippingDaos;
        if (!CollectionUtils.isEmpty(dto.getShippingStatus())) {
            shippingDaos = shippingRepository.findAllByStatusIn(dto.getShippingStatus());
        } else {
            shippingDaos = shippingRepository.findAll();
        }
        return BaseResponseUtility.getBaseResponse(shippingDaos);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse createShipping(ShippingInitiatedEvent dto) throws BadRequestException {
        Integer shippingStatusId = ShippingStatusEnum.SHIPPING.getStatusId();
        ShippingDao shippingDao = shippingRepository.findByOrderIdAndCustomerIdAndStatus(dto.getOrderId(), dto.getCustomerId(), shippingStatusId);
        if (!ObjectUtils.isEmpty(shippingDao)) {
            throw new BadRequestException(ExceptionConstants.SHIPPING_ALREADY_STARTED_FOR_THIS_ORDER);
        }
        CustomerDao customerDao = customerRepository.findById(dto.getCustomerId()).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_CUSTOMER));
        OrdersDao ordersDao = ordersRepository.findById(dto.getOrderId()).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_ORDER_ID));
        ShippingDao createShippingDao = new ShippingDao();
        createShippingDao.setCustomerId(dto.getCustomerId());
        createShippingDao.setOrderId(dto.getOrderId());
        createShippingDao.setAddress(customerDao.getAddress());
        createShippingDao.setShippedAt(LocalDateTime.now());
        createShippingDao.setStatus(shippingStatusId);
        createShippingDao = shippingRepository.save(createShippingDao);
        if (!ObjectUtils.isEmpty(createShippingDao)) {
            NotificationRequestEvent request = new NotificationRequestEvent();
            request.setStatus(shippingStatusId);
            request.setProductIds(ordersDao.getOrdersDetailsDaoList().stream().map(OrdersDetailsDao::getProductId).toList());
            request.setNotificationModuleId(ModuleEnum.SHIPPING.getModuleId());
            request.setOrderId(dto.getOrderId());
            request.setCustomerId(dto.getCustomerId());
            notificationService.sendSmsEmailPushNotifications(request);
        }
        return BaseResponseUtility.getBaseResponse(createShippingDao);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse updateShippingStatus(Long orderId,Integer shippingStatus) throws BadRequestException {
        if(!shippingStatus.equals(ShippingStatusEnum.SHIPPED.getStatusId()) && !shippingStatus.equals(ShippingStatusEnum.DELIVERED.getStatusId())){
            throw  new BadRequestException(ExceptionConstants.INVALID_SHIPPING_STATUS);
        }
        ShippingDao shippingDao = shippingRepository.findByOrderId(orderId);
        if(ObjectUtils.isEmpty(shippingDao)){
            throw  new BadRequestException(ExceptionConstants.INVALID_SHIPPING_ID);
        }
        if(shippingDao.getStatus().equals(shippingStatus)){
            throw new BadRequestException(ExceptionConstants.ORDER_ALREADY_SAME_STATUS);
        }
        OrdersDao ordersDao  = ordersRepository.findById(shippingDao.getOrderId()).orElseThrow(()->new BadRequestException(ExceptionConstants.INVALID_ORDER_ID));
        shippingDao.setStatus(shippingStatus);
        shippingDao = shippingRepository.save(shippingDao);
        if(!ObjectUtils.isEmpty(shippingDao)){
            NotificationRequestEvent request = new NotificationRequestEvent();
            request.setStatus(shippingStatus);
            request.setProductIds(ordersDao.getOrdersDetailsDaoList().stream().map(OrdersDetailsDao::getProductId).toList());
            request.setNotificationModuleId(ModuleEnum.SHIPPING.getModuleId());
            request.setOrderId(shippingDao.getOrderId());
            request.setCustomerId(shippingDao.getCustomerId());
            notificationService.sendSmsEmailPushNotifications(request);
        }

        return BaseResponseUtility.getBaseResponse(shippingDao);
    }
}

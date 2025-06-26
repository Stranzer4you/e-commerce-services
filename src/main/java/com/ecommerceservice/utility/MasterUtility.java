package com.ecommerceservice.utility;

import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.notifications.model.request.BulkNotificationRequest;
import com.ecommerceservice.notifications.service.NotificationServiceImpl;
import com.ecommerceservice.orders.dao.OrdersDao;
import com.ecommerceservice.orders.dao.OrdersDetailsDao;
import com.ecommerceservice.orders.repository.OrdersRepository;
import com.ecommerceservice.orders.service.OrdersService;
import com.ecommerceservice.orders.service.OrdersServiceImpl;
import com.ecommerceservice.payments.model.request.UpdateOrderStatusDto;
import com.ecommerceservice.utility.constants.ExceptionConstants;
import com.ecommerceservice.utility.enums.ModuleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Component
public class MasterUtility {


    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private NotificationServiceImpl notificationService;

    public void updateOrderStatusAndNotify(UpdateOrderStatusDto dto) throws BadRequestException {
        OrdersDao ordersDao  = ordersRepository.findById(dto.getOrderId()).orElseThrow(()->new BadRequestException(ExceptionConstants.INVALID_ORDER_ID));
        ordersDao.setStatus(dto.getStatus());
        ordersDao = ordersRepository.save(ordersDao);
        // send notifications
        if(!ObjectUtils.isEmpty(ordersDao)) {
            BulkNotificationRequest notificationRequest = new BulkNotificationRequest();
            notificationRequest.setNotificationModuleId(ModuleEnum.ORDERS.getModuleId());
            notificationRequest.setStatus(dto.getStatus());
            notificationRequest.setCustomerId(ordersDao.getCustomerId());
            notificationRequest.setOrderId(ordersDao.getId());
            notificationRequest.setProductIds(ordersDao.getOrdersDetailsDaoList().stream().map(OrdersDetailsDao::getProductId).toList());
            notificationRequest.setAmount(ordersDao.getTotalAmount());
            notificationService.sendSmsEmailPushNotifications(notificationRequest);
        }
    }
}

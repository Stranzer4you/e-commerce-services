package com.ecommerceservice.utility;

import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.notifications.model.request.NotificationRequestEvent;
import com.ecommerceservice.notifications.service.NotificationServiceImpl;
import com.ecommerceservice.orders.dao.OrdersDao;
import com.ecommerceservice.orders.dao.OrdersDetailsDao;
import com.ecommerceservice.orders.kafka.OrderKafkaProducer;
import com.ecommerceservice.orders.repository.OrdersRepository;
import com.ecommerceservice.payments.model.request.UpdateOrderStatusDto;
import com.ecommerceservice.utility.constants.ExceptionConstants;
import com.ecommerceservice.utility.enums.ModuleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Component
public class MasterUtility {


    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderKafkaProducer orderKafkaProducer;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void updateOrderStatusAndNotify(UpdateOrderStatusDto dto) throws BadRequestException {
        OrdersDao ordersDao  = ordersRepository.findById(dto.getOrderId()).orElseThrow(()->new BadRequestException(ExceptionConstants.INVALID_ORDER_ID));
        ordersDao.setStatus(dto.getStatus());
        ordersDao = ordersRepository.save(ordersDao);
        // send notifications
        if(!ObjectUtils.isEmpty(ordersDao)) {
            NotificationRequestEvent notificationRequest = new NotificationRequestEvent();
            notificationRequest.setNotificationModuleId(ModuleEnum.ORDERS.getModuleId());
            notificationRequest.setStatus(dto.getStatus());
            notificationRequest.setCustomerId(ordersDao.getCustomerId());
            notificationRequest.setOrderId(ordersDao.getId());
            notificationRequest.setProductIds(ordersDao.getOrdersDetailsDaoList().stream().map(OrdersDetailsDao::getProductId).toList());
            notificationRequest.setAmount(ordersDao.getTotalAmount());

            // publish to order status update topic
            orderKafkaProducer.publishToStatusUpdate(notificationRequest);
        }
    }
}

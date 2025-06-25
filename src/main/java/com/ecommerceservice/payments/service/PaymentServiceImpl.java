package com.ecommerceservice.payments.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.config.BaseResponseUtility;
import com.ecommerceservice.customers.repository.CustomerRepository;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.notifications.model.request.BulkNotificationRequest;
import com.ecommerceservice.notifications.service.NotificationServiceImpl;
import com.ecommerceservice.orders.dao.OrdersDetailsDao;
import com.ecommerceservice.orders.repository.OrdersDetailsRepository;
import com.ecommerceservice.orders.repository.OrdersRepository;
import com.ecommerceservice.payments.dao.PaymentDao;
import com.ecommerceservice.payments.mapper.PaymentMapper;
import com.ecommerceservice.payments.model.request.AllPaymentRequestDto;
import com.ecommerceservice.payments.model.request.MakePaymentRequestDto;
import com.ecommerceservice.payments.repository.PaymentRepository;
import com.ecommerceservice.utility.ExceptionConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.ecommerceservice.utility.CommonConstants.*;


@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {


    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private NotificationServiceImpl notificationService;

    @Autowired
    private OrdersDetailsRepository ordersDetailsRepository;


    @Override
    public BaseResponse getAllPayments(AllPaymentRequestDto dto) {
        List<PaymentDao> paymentDaos;
        if(!CollectionUtils.isEmpty(dto.getPaymentStatus())){
            paymentDaos = paymentRepository.findAllByStatusIn(dto.getPaymentStatus());
        }
        else{
            paymentDaos = paymentRepository.findAll();
        }
        return BaseResponseUtility.getBaseResponse(paymentDaos);
    }

    @Override
    public BaseResponse makePayment(MakePaymentRequestDto dto) throws BadRequestException {
        Boolean isCustomerExists = customerRepository.existsById(dto.getCustomerId());
        if(Boolean.FALSE.equals(isCustomerExists)){
            throw new BadRequestException(ExceptionConstants.INVALID_CUSTOMER);
        }
        Boolean isOrderExists = ordersRepository.existsById(dto.getOrderId());
        if(Boolean.FALSE.equals(isOrderExists)){
            throw new BadRequestException(ExceptionConstants.INVALID_ORDER_ID);
        }
        PaymentDao paymentDao = paymentMapper.paymetDtoToPaymentDao(dto);
        paymentDao.setPaymentTime(LocalDateTime.now());
        paymentDao.setStatus(PENDING_STATUS_ID);
        paymentDao = paymentRepository.save(paymentDao);
        if(!ObjectUtils.isEmpty(paymentDao)){
            BulkNotificationRequest notificationRequest = new BulkNotificationRequest();
            notificationRequest.setNotificationModuleId(PAYMENT_MODULE_ID);
            notificationRequest.setStatus(PROCESSING_STATUS_ID);
            notificationRequest.setCustomerId(dto.getCustomerId());
            notificationRequest.setOrderId(dto.getOrderId());
            notificationRequest.setAmount(dto.getAmount());
            List<Long> productIds = ordersDetailsRepository.findAllByOrderId(dto.getOrderId()).stream().map(OrdersDetailsDao::getProductId).toList();
            notificationRequest.setProductIds(productIds);
            notificationService.sendSmsEmailPushNotifications(notificationRequest);
        }
        return BaseResponseUtility.getBaseResponse(paymentDao);
    }
}

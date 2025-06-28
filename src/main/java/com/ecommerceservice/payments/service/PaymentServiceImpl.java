package com.ecommerceservice.payments.service;

import com.ecommerceservice.inventory.service.InventoryServiceImpl;
import com.ecommerceservice.orders.dao.OrdersDao;
import com.ecommerceservice.orders.model.request.InventoryUpdateEvent;
import com.ecommerceservice.payments.kafka.PaymentProducer;
import com.ecommerceservice.shipping.model.request.ShippingInitiatedEvent;
import com.ecommerceservice.shipping.service.ShippingServiceImpl;
import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.utility.BaseResponseUtility;
import com.ecommerceservice.customers.repository.CustomerRepository;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.notifications.model.request.NotificationRequestEvent;
import com.ecommerceservice.notifications.service.NotificationServiceImpl;
import com.ecommerceservice.orders.dao.OrdersDetailsDao;
import com.ecommerceservice.orders.repository.OrdersDetailsRepository;
import com.ecommerceservice.orders.repository.OrdersRepository;
import com.ecommerceservice.payments.dao.PaymentDao;
import com.ecommerceservice.payments.mapper.PaymentMapper;
import com.ecommerceservice.payments.model.request.AllPaymentRequestDto;
import com.ecommerceservice.payments.model.request.PaymentInitiatedEvent;
import com.ecommerceservice.payments.model.request.UpdateOrderStatusDto;
import com.ecommerceservice.payments.repository.PaymentRepository;
import com.ecommerceservice.utility.MasterUtility;
import com.ecommerceservice.utility.constants.ExceptionConstants;
import com.ecommerceservice.utility.enums.ModuleEnum;
import com.ecommerceservice.utility.enums.OrderStatusEnum;
import com.ecommerceservice.utility.enums.PaymentStatusEnum;
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

    @Autowired
    private MasterUtility masterUtility;

    @Autowired
    private InventoryServiceImpl inventoryService;

    @Autowired
    private ShippingServiceImpl shippingService;

    @Autowired
    private PaymentProducer paymentProducer;


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

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse makePayment(PaymentInitiatedEvent dto) throws BadRequestException {
        Boolean isCustomerExists = customerRepository.existsById(dto.getCustomerId());
        if(Boolean.FALSE.equals(isCustomerExists)){
            throw new BadRequestException(ExceptionConstants.INVALID_CUSTOMER);
        }
        OrdersDao ordersDao = ordersRepository.findById(dto.getOrderId()).orElseThrow(()->new BadRequestException(ExceptionConstants.INVALID_ORDER_ID));
        PaymentDao paymentDao = paymentMapper.paymetDtoToPaymentDao(dto);
        paymentDao.setPaymentTime(LocalDateTime.now());
        PaymentStatusEnum paymentStatusEnum = PaymentStatusEnum.simulate();
        Integer paymentStatus = paymentStatusEnum.getStatusId();
        paymentDao.setStatus(paymentStatus);

        paymentDao = paymentRepository.save(paymentDao);
        if(!ObjectUtils.isEmpty(paymentDao)) {
            NotificationRequestEvent notificationRequest = new NotificationRequestEvent();
            notificationRequest.setNotificationModuleId(ModuleEnum.PAYMENTS.getModuleId());
            notificationRequest.setStatus(paymentStatus);
            notificationRequest.setCustomerId(dto.getCustomerId());
            notificationRequest.setOrderId(dto.getOrderId());
            notificationRequest.setAmount(dto.getAmount());
            List<Long> productIds = ordersDetailsRepository.findAllByOrderId(dto.getOrderId()).stream().map(OrdersDetailsDao::getProductId).toList();
            notificationRequest.setProductIds(productIds);

            if(paymentStatus.equals(PaymentStatusEnum.SUCCESS.getStatusId())){
                //publish to payment success topic
                UpdateOrderStatusDto updateOrderStatusDto = generateOrderUpdateStatusRequest(dto.getOrderId(), OrderStatusEnum.PROCESSED.getStatusId());
                paymentProducer.publishToPaymentSuccessTopic(notificationRequest);

                // update inventory
                InventoryUpdateEvent inventoryUpdateEvent = new InventoryUpdateEvent();
                inventoryUpdateEvent.setOrdersDetailsDaoList(ordersDao.getOrdersDetailsDaoList());
                paymentProducer.publishToInventoryUpdateTopic(inventoryUpdateEvent);

                // create shipping
                ShippingInitiatedEvent shippingInitiatedEvent = new ShippingInitiatedEvent();
                shippingInitiatedEvent.setCustomerId(dto.getCustomerId());
                shippingInitiatedEvent.setOrderId(dto.getOrderId());

                // publish to create shipping topic
                paymentProducer.publishToShippingInitiateTopic(shippingInitiatedEvent);
            }
            else if(paymentStatus.equals(PaymentStatusEnum.FAILED.getStatusId())){
                UpdateOrderStatusDto updateOrderStatusDto = generateOrderUpdateStatusRequest(dto.getOrderId(),OrderStatusEnum.FAILED.getStatusId());

                //publish to payment failed topic
                paymentProducer.publishToPaymentFailedTopic(notificationRequest);
            }
            else{
                //publish to payment pending topic
                paymentProducer.publishToPaymentPendingTopic(notificationRequest);
            }
        }
        return BaseResponseUtility.getBaseResponse(paymentDao);
    }

    public UpdateOrderStatusDto generateOrderUpdateStatusRequest(Long orderId,Integer status){
        UpdateOrderStatusDto dto = new UpdateOrderStatusDto();
        dto.setOrderId(orderId);
        dto.setStatus(status);
        return  dto;
    }
}

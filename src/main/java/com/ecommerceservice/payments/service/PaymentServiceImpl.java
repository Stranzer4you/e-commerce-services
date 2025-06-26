package com.ecommerceservice.payments.service;

import com.ecommerceservice.inventory.service.InventoryServiceImpl;
import com.ecommerceservice.orders.dao.OrdersDao;
import com.ecommerceservice.shipping.model.request.CreateShippingRequestDto;
import com.ecommerceservice.shipping.service.ShippingServiceImpl;
import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.utility.BaseResponseUtility;
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
import com.ecommerceservice.payments.model.request.UpdateOrderStatusDto;
import com.ecommerceservice.payments.repository.PaymentRepository;
import com.ecommerceservice.utility.MasterUtility;
import com.ecommerceservice.utility.constants.ExceptionConstants;
import com.ecommerceservice.utility.enums.ModuleEnum;
import com.ecommerceservice.utility.enums.OrderStatusEnum;
import com.ecommerceservice.utility.enums.PaymentStatusEnum;
import com.ecommerceservice.utility.enums.ShippingStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        OrdersDao ordersDao = ordersRepository.findById(dto.getOrderId()).orElseThrow(()->new BadRequestException(ExceptionConstants.INVALID_ORDER_ID));
        PaymentDao paymentDao = paymentMapper.paymetDtoToPaymentDao(dto);
        paymentDao.setPaymentTime(LocalDateTime.now());
        PaymentStatusEnum paymentStatusEnum = PaymentStatusEnum.simulate();
        Integer paymentStatus = paymentStatusEnum.getStatusId();
        paymentDao.setStatus(paymentStatus);

        paymentDao = paymentRepository.save(paymentDao);
        if(!ObjectUtils.isEmpty(paymentDao)) {
            BulkNotificationRequest notificationRequest = new BulkNotificationRequest();
            notificationRequest.setNotificationModuleId(ModuleEnum.PAYMENTS.getModuleId());
            notificationRequest.setStatus(paymentStatus);
            notificationRequest.setCustomerId(dto.getCustomerId());
            notificationRequest.setOrderId(dto.getOrderId());
            notificationRequest.setAmount(dto.getAmount());
            List<Long> productIds = ordersDetailsRepository.findAllByOrderId(dto.getOrderId()).stream().map(OrdersDetailsDao::getProductId).toList();
            notificationRequest.setProductIds(productIds);
            notificationService.sendSmsEmailPushNotifications(notificationRequest);

            if(paymentStatus.equals(PaymentStatusEnum.SUCCESS.getStatusId())){
                // update order status
                UpdateOrderStatusDto updateOrderStatusDto = generateOrderUpdateStatusRequest(dto.getOrderId(), OrderStatusEnum.PROCESSED.getStatusId());
                masterUtility.updateOrderStatusAndNotify(updateOrderStatusDto);
                // update inventory
                Map<Long,Integer> inventoryUpdateRequest = new HashMap<>();
                ordersDao.getOrdersDetailsDaoList().forEach(x->{
                    inventoryUpdateRequest.put(x.getProductId(), x.getQuantity());
                });
                inventoryService.updateProductQuantities(inventoryUpdateRequest);
                // create shipping
                CreateShippingRequestDto createShippingRequestDto = new CreateShippingRequestDto();
                createShippingRequestDto.setCustomerId(dto.getCustomerId());
                createShippingRequestDto.setOrderId(dto.getOrderId());
                shippingService.createShipping(createShippingRequestDto);
            }
            else if(paymentStatus.equals(PaymentStatusEnum.FAILED.getStatusId())){
                UpdateOrderStatusDto updateOrderStatusDto = generateOrderUpdateStatusRequest(dto.getOrderId(),OrderStatusEnum.FAILED.getStatusId());
                masterUtility.updateOrderStatusAndNotify(updateOrderStatusDto);
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

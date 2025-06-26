package com.ecommerceservice.orders.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.config.BaseResponseUtility;
import com.ecommerceservice.customers.dao.CustomerDao;
import com.ecommerceservice.customers.repository.CustomerRepository;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.inventory.dao.Product;
import com.ecommerceservice.inventory.repository.InventoryRepository;
import com.ecommerceservice.notifications.model.request.BulkNotificationRequest;
import com.ecommerceservice.notifications.service.NotificationServiceImpl;
import com.ecommerceservice.orders.dao.OrdersDao;
import com.ecommerceservice.orders.dao.OrdersDetailsDao;
import com.ecommerceservice.orders.mapper.OrdersMapper;
import com.ecommerceservice.orders.model.request.AllOrdersRequestDto;
import com.ecommerceservice.orders.model.request.CreateOrderRequestDto;
import com.ecommerceservice.orders.model.request.OrdersDetailRequestDto;
import com.ecommerceservice.orders.repository.OrdersDetailsRepository;
import com.ecommerceservice.orders.repository.OrdersRepository;
import com.ecommerceservice.payments.model.request.MakePaymentRequestDto;
import com.ecommerceservice.payments.model.request.UpdateOrderStatusDto;
import com.ecommerceservice.payments.service.PaymentServiceImpl;
import com.ecommerceservice.utility.constants.ExceptionConstants;
import com.ecommerceservice.utility.enums.ModuleEnum;
import com.ecommerceservice.utility.enums.OrderStatusEnum;
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
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private NotificationServiceImpl notificationService;

    @Autowired
    private PaymentServiceImpl paymentService;

    @Autowired
    private OrdersDetailsRepository ordersDetailsRepository;


    @Override
    public BaseResponse createOrder(CreateOrderRequestDto dto) throws BadRequestException {
        CustomerDao customerDao = customerRepository.findById(dto.getCustomerId()).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_CUSTOMER));
        Double totalPaidAmount = dto.getTotalAmount();
        Double sumOfEachProductPaidAmount = dto.getOrdersDetails().stream().mapToDouble(OrdersDetailRequestDto::getAmountPaid).sum();
        if(!Objects.equals(totalPaidAmount,sumOfEachProductPaidAmount)){
            throw new BadRequestException(ExceptionConstants.TOTAL_PAID_AMOUNT_NOT_MATCHING);
        }
        List<Long> productIds = dto.getOrdersDetails().stream().map(OrdersDetailRequestDto::getProductId).toList();
        List<Product> products = inventoryRepository.findAllById(productIds);
        if (ObjectUtils.isEmpty(products) || products.size() != productIds.size()) {
            throw new BadRequestException(ExceptionConstants.INVALID_PRODUCT_IDS);
        }
        // check inventory of each product
        Map<Long,Integer> inventoryProductIdQuantityMap = products.stream().collect(Collectors.toMap(Product::getId, Product::getQuantity));
        Map<Long,Integer> customerProductIdQuantityMap = dto.getOrdersDetails().stream().collect(Collectors.toMap(OrdersDetailRequestDto::getProductId,OrdersDetailRequestDto::getQuantity));
        for(Map.Entry<Long,Integer> map : inventoryProductIdQuantityMap.entrySet()){
            Long productId = map.getKey();
            Integer quantity = map.getValue();
            Integer requestedQuantity = customerProductIdQuantityMap.get(productId);
            if(requestedQuantity > quantity){
                throw new BadRequestException(ExceptionConstants.PRODUCT_OUT_OF_STOCK);
            }
        }

        //  check if amount paid for each object is proper
        Map<Long, Double> productPricesMap = products.stream().collect(Collectors.toMap(Product::getId, Product::getPrice));
        Map<Long, Double> customerPaidMap = dto.getOrdersDetails().stream().collect(Collectors.toMap(OrdersDetailRequestDto::getProductId, OrdersDetailRequestDto::getAmountPaid));
        for (Map.Entry<Long, Double> map : customerPaidMap.entrySet()) {
            Long productId = map.getKey();
            Double userPaidAmount = map.getValue();
            Double productPrice = productPricesMap.get(productId);
            if (!Objects.equals(userPaidAmount, productPrice)) {
                throw new BadRequestException(ExceptionConstants.CUSTOMER_NOT_PAID_FULL_AMOUNT);
            }
        }
        OrdersDao ordersDao = new OrdersDao();
        ordersDao.setCreatedAt(LocalDateTime.now());
        ordersDao.setStatus(OrderStatusEnum.PROCESSING.getStatusId());
        ordersDao.setTotalAmount(dto.getTotalAmount());
        ordersDao.setCustomerId(dto.getCustomerId());
        List<OrdersDetailsDao> ordersDetailsDaoList = new ArrayList<>();
        dto.getOrdersDetails().forEach(ordersDetailRequestDto -> {
            OrdersDetailsDao ordersDetailsDao = new OrdersDetailsDao();
            ordersDetailsDao.setAmountPaid(ordersDetailRequestDto.getAmountPaid());
            ordersDetailsDao.setQuantity(ordersDetailRequestDto.getQuantity());
            ordersDetailsDao.setProductId(ordersDetailRequestDto.getProductId());
            ordersDetailsDaoList.add(ordersDetailsDao);
        });
        ordersDao.setOrdersDetailsDaoList(ordersDetailsDaoList);
        ordersDao = ordersRepository.save(ordersDao);
        // send notification upon order creating
        OrdersDao finalOrdersDao = ordersDao;
        if (!ObjectUtils.isEmpty(ordersDao)) {
            BulkNotificationRequest notificationRequest = new BulkNotificationRequest();
            notificationRequest.setNotificationModuleId(ModuleEnum.ORDERS.getModuleId());
            notificationRequest.setStatus(OrderStatusEnum.PROCESSING.getStatusId());
            notificationRequest.setCustomerId(dto.getCustomerId());
            notificationRequest.setOrderId(ordersDao.getId());
            notificationRequest.setProductIds(dto.getOrdersDetails().stream().map(OrdersDetailRequestDto::getProductId).toList());
            notificationRequest.setAmount(dto.getTotalAmount());
            notificationService.sendSmsEmailPushNotifications(notificationRequest);
           // make payment
            MakePaymentRequestDto makePaymentRequestDto = new MakePaymentRequestDto();
            makePaymentRequestDto.setAmount(dto.getTotalAmount());
            makePaymentRequestDto.setOrderId(ordersDao.getId());
            makePaymentRequestDto.setCustomerId(dto.getCustomerId());
            paymentService.makePayment(makePaymentRequestDto);
        }

        ordersDao.getOrdersDetailsDaoList().forEach(ordersDetailsDao -> {
            ordersDetailsDao.setOrderId(finalOrdersDao.getId());
        });
        return BaseResponseUtility.getBaseResponse(ordersDao);
    }

    @Override
    public BaseResponse getAllOrders(AllOrdersRequestDto dto) {
        List<OrdersDao> ordersDaos;
        if(!CollectionUtils.isEmpty(dto.getOrderStatus())){
            ordersDaos = ordersRepository.findAllByStatusIn(dto.getOrderStatus());
        }
        else{
            ordersDaos = ordersRepository.findAll();
        }
        return BaseResponseUtility.getBaseResponse(ordersDaos);
    }

    @Override
    public BaseResponse getOrderById(Long orderId) {
        Optional<OrdersDao> ordersDao = ordersRepository.findById(orderId);
        if(ordersDao.isPresent()){
            return BaseResponseUtility.getBaseResponse(ordersDao.get());
        }
        return  BaseResponseUtility.getBaseResponse();

    }

    @Override
    public BaseResponse updateOrderStatus(UpdateOrderStatusDto dto) throws BadRequestException {
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
        return BaseResponseUtility.getBaseResponse(ordersDao);
    }


    public List<OrdersDetailsDao> getOrderDetailsByOrderId(Long orderId){
        return ordersDetailsRepository.findAllByOrderId(orderId);
    }

}

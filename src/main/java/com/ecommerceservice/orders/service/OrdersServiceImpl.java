package com.ecommerceservice.orders.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.config.BaseResponseUtility;
import com.ecommerceservice.customers.dao.CustomerDao;
import com.ecommerceservice.customers.repository.CustomerRepository;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.inventory.dao.Products;
import com.ecommerceservice.inventory.repository.InventoryRepository;
import com.ecommerceservice.notifications.dao.NotificationDao;
import com.ecommerceservice.notifications.model.request.CreateNotificationRequestDto;
import com.ecommerceservice.notifications.service.NotificationService;
import com.ecommerceservice.orders.dao.OrdersDao;
import com.ecommerceservice.orders.dao.OrdersDetailsDao;
import com.ecommerceservice.orders.mapper.OrdersMapper;
import com.ecommerceservice.orders.model.request.AllOrdersRequestDto;
import com.ecommerceservice.orders.model.request.CreateOrderRequestDto;
import com.ecommerceservice.orders.model.request.OrdersDetailRequestDto;
import com.ecommerceservice.orders.repository.OrdersRepository;
import com.ecommerceservice.utility.CommonConstants;
import com.ecommerceservice.utility.ExceptionConstants;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ecommerceservice.utility.CommonConstants.*;


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
    private NotificationService notificationService;



    @Override
    public BaseResponse createOrder(CreateOrderRequestDto dto) throws BadRequestException {
        CustomerDao customerDao = customerRepository.findById(dto.getCustomerId()).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_CUSTOMER));
        Double totalPaidAmount = dto.getTotalAmount();
        Double sumOfEachProductPaidAmount = dto.getOrdersDetails().stream().mapToDouble(OrdersDetailRequestDto::getAmountPaid).sum();
        if(!Objects.equals(totalPaidAmount,sumOfEachProductPaidAmount)){
            throw new BadRequestException(ExceptionConstants.TOTAL_PAID_AMOUNT_NOT_MATCHING);
        }
        List<Long> productIds = dto.getOrdersDetails().stream().map(OrdersDetailRequestDto::getProductId).toList();
        List<Products> products = inventoryRepository.findAllById(productIds);
        if (ObjectUtils.isEmpty(products) || products.size() != productIds.size()) {
            throw new BadRequestException(ExceptionConstants.INVALID_PRODUCT_IDS);
        }
        // check inventory of each product
        Map<Long,Integer> inventoryProductIdQuantityMap = products.stream().collect(Collectors.toMap(Products::getId,Products::getQuantity));
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
        Map<Long, Double> productPricesMap = products.stream().collect(Collectors.toMap(Products::getId, Products::getPrice));
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
        ordersDao.setStatus(CommonConstants.PROCESSING_STATUS_ID);
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
            List<CreateNotificationRequestDto> notifications = new ArrayList<>();
            notificationTypeIds.forEach(notification -> {
                CreateNotificationRequestDto dao = new CreateNotificationRequestDto();
                dao.setOrderId(finalOrdersDao.getId());
                dao.setStatus(PROCESSING_STATUS_ID);
                dao.setCustomerId(dto.getCustomerId());
                dao.setMessage("your order is being processed");
                dao.setNotificationModuleId(ORDER_MODULE_ID);
                dao.setNotifyTime(LocalDateTime.now());
                notifications.add(dao);
            });
            if(!CollectionUtils.isEmpty(notifications)){
                notificationService.createBulkNotifications(notifications);
            }
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

}

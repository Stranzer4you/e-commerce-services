package com.ecommerceservice.orders.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.config.BaseResponseUtility;
import com.ecommerceservice.customers.dao.CustomerDao;
import com.ecommerceservice.customers.repository.CustomerRepository;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.inventory.dao.Products;
import com.ecommerceservice.inventory.repository.InventoryRepository;
import com.ecommerceservice.orders.dao.OrdersDao;
import com.ecommerceservice.orders.dao.OrdersDetailsDao;
import com.ecommerceservice.orders.mapper.OrdersMapper;
import com.ecommerceservice.orders.model.request.CreateOrderRequestDto;
import com.ecommerceservice.orders.model.request.OrdersDetailRequestDto;
import com.ecommerceservice.orders.repository.OrdersRepository;
import com.ecommerceservice.utility.CommonConstants;
import com.ecommerceservice.utility.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrdersMapper ordersMapper;


    @Override
    public BaseResponse createOrder(CreateOrderRequestDto dto) throws BadRequestException {
        CustomerDao customerDao = customerRepository.findById(dto.getCustomerId()).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_CUSTOMER));
        List<Long> productIds = dto.getOrdersDetails().stream().map(OrdersDetailRequestDto::getProductId).toList();
        List<Products> products = inventoryRepository.findAllById(productIds);
        if (ObjectUtils.isEmpty(products) || products.size() != productIds.size()) {
            throw new BadRequestException(ExceptionConstants.INVALID_PRODUCT_IDS);
        }
        Map<Long, Double> productPricesMap = products.stream().collect(Collectors.toMap(Products::getId, Products::getPrice));
        Map<Long, Double> customerPaidMap = dto.getOrdersDetails().stream().collect(Collectors.toMap(OrdersDetailRequestDto::getProductId, OrdersDetailRequestDto::getAmountPaid));
        for (Map.Entry<Long, Double> map : customerPaidMap.entrySet()) {
            Long productId = map.getKey();
            Double userPaidAmount = map.getValue();
            Double productPrice = productPricesMap.get(productId);
            if (userPaidAmount < productPrice) {
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
            ordersDetailsDao.setQuantity(ordersDetailsDao.getQuantity());
            ordersDetailsDao.setProductId(ordersDetailsDao.getProductId());
            ordersDetailsDaoList.add(ordersDetailsDao);
        });
        ordersDao.setOrdersDetailsDaoList(ordersDetailsDaoList);
        ordersDao = ordersRepository.save(ordersDao);
        return BaseResponseUtility.getBaseResponse(ordersDao);
    }
}

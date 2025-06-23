package com.ecommerceservice.orders.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.orders.model.request.AllOrdersRequestDto;
import com.ecommerceservice.orders.model.request.CreateOrderRequestDto;

import javax.validation.Valid;

public interface OrdersService {

    BaseResponse createOrder( CreateOrderRequestDto dto) throws BadRequestException;

    BaseResponse getAllOrders(AllOrdersRequestDto dto);

    BaseResponse getOrderById(Long orderId);
}

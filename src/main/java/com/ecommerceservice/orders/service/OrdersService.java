package com.ecommerceservice.orders.service;

import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.orders.model.request.AllOrdersRequestDto;
import com.ecommerceservice.orders.model.request.CreateOrderRequestDto;
import com.ecommerceservice.payments.model.request.UpdateOrderStatusDto;

public interface OrdersService {

    BaseResponse createOrder( CreateOrderRequestDto dto) throws BadRequestException;

    BaseResponse getAllOrders(AllOrdersRequestDto dto);

    BaseResponse getOrderById(Long orderId);

    BaseResponse updateOrderStatus(UpdateOrderStatusDto dto) throws BadRequestException;
}

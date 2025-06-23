package com.ecommerceservice.orders.controller;


import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.orders.model.request.AllOrdersRequestDto;
import com.ecommerceservice.orders.model.request.CreateOrderRequestDto;
import com.ecommerceservice.orders.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/create-order")
    public BaseResponse createOrder(@Valid @RequestBody CreateOrderRequestDto dto) throws BadRequestException {
        return ordersService.createOrder(dto);
    }

    @GetMapping("/all-orders")
    public BaseResponse getAllOrders(@ModelAttribute AllOrdersRequestDto dto){
        return ordersService.getAllOrders(dto);
    }

    @GetMapping("/order/{orderId}")
    public BaseResponse getOrderById(@PathVariable("orderId") Long orderId){
        return  ordersService.getOrderById(orderId);
    }

}


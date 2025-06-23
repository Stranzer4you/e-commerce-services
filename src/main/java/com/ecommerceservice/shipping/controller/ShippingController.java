package com.ecommerceservice.shipping.controller;


import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.shipping.model.request.ShippingRequestDto;
import com.ecommerceservice.shipping.model.request.CreateShippingRequestDto;
import com.ecommerceservice.shipping.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @GetMapping()
    public BaseResponse getAllShippings(@Valid @ModelAttribute ShippingRequestDto dto) {
        return shippingService.getAllShippings(dto);
    }

    @PostMapping()
    public BaseResponse createShipping(@Valid @RequestBody CreateShippingRequestDto dto) throws BadRequestException {
        return shippingService.createShipping(dto);
    }

}


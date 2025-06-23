package com.ecommerceservice.shipping.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.shipping.model.request.ShippingRequestDto;
import com.ecommerceservice.shipping.model.request.CreateShippingRequestDto;

import javax.validation.Valid;

public interface ShippingService {

    BaseResponse getAllShippings(@Valid ShippingRequestDto dto);

    BaseResponse createShipping(CreateShippingRequestDto dto) throws BadRequestException;
}

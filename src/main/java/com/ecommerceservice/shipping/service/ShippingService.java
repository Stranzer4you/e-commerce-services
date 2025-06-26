package com.ecommerceservice.shipping.service;

import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.shipping.model.request.ShippingRequestDto;
import com.ecommerceservice.shipping.model.request.CreateShippingRequestDto;

import javax.validation.Valid;

public interface ShippingService {

    BaseResponse getAllShippings(@Valid ShippingRequestDto dto);

    BaseResponse createShipping(CreateShippingRequestDto dto) throws BadRequestException;

    BaseResponse updateShippingStatus(Long orderId,Integer shippingStatus) throws BadRequestException;
}

package com.ecommerceservice.customers.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.customers.model.request.AddCustomerRequest;
import com.ecommerceservice.exceptions.BadRequestException;

import javax.validation.Valid;

public interface CustomerService {
    BaseResponse getAllCustomers();

    BaseResponse addCustomer( AddCustomerRequest addCustomerRequest) throws BadRequestException;
}

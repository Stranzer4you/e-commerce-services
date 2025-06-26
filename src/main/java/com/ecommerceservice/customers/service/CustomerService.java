package com.ecommerceservice.customers.service;

import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.customers.model.request.AddCustomerRequest;
import com.ecommerceservice.exceptions.BadRequestException;

public interface CustomerService {
    BaseResponse getAllCustomers();

    BaseResponse addCustomer( AddCustomerRequest addCustomerRequest) throws BadRequestException;

    BaseResponse getCustomerById(Long customerId) throws BadRequestException;
}

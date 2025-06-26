package com.ecommerceservice.customers.controller;

import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.customers.model.request.AddCustomerRequest;
import com.ecommerceservice.customers.service.CustomerService;
import com.ecommerceservice.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomersController {

    @Autowired
    private CustomerService customerService;

    @GetMapping()
    public BaseResponse getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @PostMapping()
    public BaseResponse addCustomer(@Valid @RequestBody AddCustomerRequest addCustomerRequest) throws BadRequestException {
        return customerService.addCustomer(addCustomerRequest);
    }

    @GetMapping("{customerId}")
    public BaseResponse getCustomerById(@PathVariable("customerId") Long customerId) throws BadRequestException {
        return customerService.getCustomerById(customerId);
    }
}

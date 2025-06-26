package com.ecommerceservice.customers.service;

import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.utility.BaseResponseUtility;
import com.ecommerceservice.customers.dao.CustomerDao;
import com.ecommerceservice.customers.mapper.CustomerMapper;
import com.ecommerceservice.customers.model.request.AddCustomerRequest;
import com.ecommerceservice.customers.model.response.CustomerResponse;
import com.ecommerceservice.customers.repository.CustomerRepository;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.utility.constants.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public BaseResponse getAllCustomers() {
        List<CustomerDao> customerDaoList = customerRepository.findAll();
        List<CustomerResponse> customerResponses = customerMapper.customersDaosToCustomerResponseList(customerDaoList);
        return BaseResponseUtility.getBaseResponse(customerResponses);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public BaseResponse addCustomer(AddCustomerRequest addCustomerRequest) throws BadRequestException {
        Boolean isEmailExists = customerRepository.existsByEmail(addCustomerRequest.getEmail());
        if(Boolean.TRUE.equals(isEmailExists)){
            throw  new BadRequestException(ExceptionConstants.CUSTOMER_EMAIL_ALREADY_EXISTS);
        }
        Boolean isPhoneNumberExists = customerRepository.existsByPhoneNumber(addCustomerRequest.getPhoneNumber());
        if (Boolean.TRUE.equals(isPhoneNumberExists)){
            throw new BadRequestException(ExceptionConstants.CUSTOMER_PHONE_NUMBER_ALREADY_EXISTS);
        }
        CustomerDao customerDao = customerMapper.customerRequesttoCustomerDao(addCustomerRequest);
        customerDao = customerRepository.save(customerDao);
        return  BaseResponseUtility.getBaseResponse(customerDao);
    }

    @Override
    public BaseResponse getCustomerById(Long customerId) throws BadRequestException {
        CustomerDao customerDao = customerRepository.findByIdAndIsActiveTrue(customerId);
        if(ObjectUtils.isEmpty(customerDao)){
            throw new BadRequestException(ExceptionConstants.INVALID_CUSTOMER);
        }
        return BaseResponseUtility.getBaseResponse(customerDao);
    }

}

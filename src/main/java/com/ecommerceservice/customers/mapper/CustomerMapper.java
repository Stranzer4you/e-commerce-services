package com.ecommerceservice.customers.mapper;


import com.ecommerceservice.customers.dao.CustomerDao;
import com.ecommerceservice.customers.model.request.AddCustomerRequest;
import com.ecommerceservice.customers.model.response.CustomerResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponse customerDaoToCustomerResponse(CustomerDao customerDao);
    List<CustomerResponse> customersDaosToCustomerResponseList(List<CustomerDao> customerDaoList);

    CustomerDao customerRequesttoCustomerDao(AddCustomerRequest addCustomerRequest);
}

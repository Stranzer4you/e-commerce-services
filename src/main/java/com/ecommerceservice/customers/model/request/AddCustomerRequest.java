package com.ecommerceservice.customers.model.request;

import lombok.Data;
import com.ecommerceservice.utility.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Valid
public class AddCustomerRequest {

    @NotEmpty(message = ExceptionConstants.CUSTOMER_NAME_SHOULD_NOT_BE_EMPTY)
    private String customerName;
    @NotEmpty(message = ExceptionConstants.CUSTOMER_PHONE_NUMBER_SHOULD_NOT_BE_EMPTY)
    private String phoneNumber;
    @NotEmpty(message = ExceptionConstants.CUSTOMER_EMAIL_SHOULD_NOT_BE_EMPTY)
    @Email(message = ExceptionConstants.INVALID_EMAIL_FORMAT)
    private String email;
    @NotEmpty(message = ExceptionConstants.CUSTOMER_ADDRESS_SHOULD_NOT_BE_EMPTY)
    private String address;
}

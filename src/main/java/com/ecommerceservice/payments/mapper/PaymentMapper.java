package com.ecommerceservice.payments.mapper;

import com.ecommerceservice.payments.dao.PaymentDao;
import com.ecommerceservice.payments.model.request.PaymentInitiatedEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDao paymetDtoToPaymentDao(PaymentInitiatedEvent dto);
}

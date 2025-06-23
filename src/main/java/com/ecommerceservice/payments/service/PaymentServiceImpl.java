package com.ecommerceservice.payments.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.config.BaseResponseUtility;
import com.ecommerceservice.customers.repository.CustomerRepository;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.inventory.repository.InventoryRepository;
import com.ecommerceservice.orders.repository.OrdersRepository;
import com.ecommerceservice.payments.dao.PaymentDao;
import com.ecommerceservice.payments.mapper.PaymentMapper;
import com.ecommerceservice.payments.model.request.AllPaymentRequestDto;
import com.ecommerceservice.payments.model.request.makePaymentRequestDto;
import com.ecommerceservice.payments.repository.PaymentRepository;
import com.ecommerceservice.utility.CommonConstants;
import com.ecommerceservice.utility.ExceptionConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {


    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrdersRepository ordersRepository;


    @Override
    public BaseResponse getAllPayments(AllPaymentRequestDto dto) {
        List<PaymentDao> paymentDaos;
        if(!CollectionUtils.isEmpty(dto.getPaymentStatus())){
            paymentDaos = paymentRepository.findAllByStatusIn(dto.getPaymentStatus());
        }
        else{
            paymentDaos = paymentRepository.findAll();
        }
        return BaseResponseUtility.getBaseResponse(paymentDaos);
    }

    @Override
    public BaseResponse makePayment(makePaymentRequestDto dto) throws BadRequestException {
        Boolean isCustomerExists = customerRepository.existsById(dto.getCustomerId());
        if(Boolean.FALSE.equals(isCustomerExists)){
            throw new BadRequestException(ExceptionConstants.INVALID_CUSTOMER);
        }
        Boolean isOrderExists = ordersRepository.existsById(dto.getOrderId());
        if(Boolean.FALSE.equals(isOrderExists)){
            throw new BadRequestException(ExceptionConstants.INVALID_ORDER_ID);
        }
        PaymentDao paymentDao = paymentMapper.paymetDtoToPaymentDao(dto);
        paymentDao.setPaymentTime(LocalDateTime.now());
        paymentDao.setStatus(CommonConstants.SUCCESS_STATUS_ID);
        paymentDao = paymentRepository.save(paymentDao);
        return BaseResponseUtility.getBaseResponse(paymentDao);
    }
}

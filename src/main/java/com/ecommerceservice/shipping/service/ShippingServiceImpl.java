package com.ecommerceservice.shipping.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.config.BaseResponseUtility;
import com.ecommerceservice.customers.repository.CustomerRepository;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.orders.repository.OrdersRepository;
import com.ecommerceservice.shipping.dao.ShippingDao;
import com.ecommerceservice.shipping.mapper.ShippingMapper;
import com.ecommerceservice.shipping.model.request.ShippingRequestDto;
import com.ecommerceservice.shipping.model.request.CreateShippingRequestDto;
import com.ecommerceservice.shipping.repository.ShippingRepository;
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
public class ShippingServiceImpl implements ShippingService {


    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrdersRepository ordersRepository;


    @Override
    public BaseResponse getAllShippings(ShippingRequestDto dto) {
        List<ShippingDao> shippingDaos;
        if(!CollectionUtils.isEmpty(dto.getShippingStatus())){
            shippingDaos = shippingRepository.findAllByStatusIn(dto.getShippingStatus());
        }
        else{
            shippingDaos = shippingRepository.findAll();
        }
        return BaseResponseUtility.getBaseResponse(shippingDaos);
    }

    @Override
    public BaseResponse createShipping(CreateShippingRequestDto dto) throws BadRequestException {
        Boolean isCustomerExists = customerRepository.existsById(dto.getCustomerId());
        if(Boolean.FALSE.equals(isCustomerExists)){
            throw new BadRequestException(ExceptionConstants.INVALID_CUSTOMER);
        }
        Boolean isOrderExists = ordersRepository.existsById(dto.getOrderId());
        if(Boolean.FALSE.equals(isOrderExists)){
            throw new BadRequestException(ExceptionConstants.INVALID_ORDER_ID);
        }
        ShippingDao shippingDao = shippingMapper.shippingDtoToShippingDao(dto);
        shippingDao.setShippedAt(LocalDateTime.now());
        shippingDao = shippingRepository.save(shippingDao);
        return BaseResponseUtility.getBaseResponse(shippingDao);
    }
}

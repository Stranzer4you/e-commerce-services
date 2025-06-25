package com.ecommerceservice.inventory.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.config.BaseResponseUtility;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.inventory.dao.Products;
import com.ecommerceservice.inventory.mapper.InventoryMapper;
import com.ecommerceservice.inventory.model.request.AddProductRequestDTO;
import com.ecommerceservice.inventory.model.response.ProductResponseDTO;
import com.ecommerceservice.inventory.repository.InventoryRepository;
import com.ecommerceservice.utility.constants.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService{

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    public BaseResponse getAllProducts() {

        List<Products> productsList = inventoryRepository.findAll();
        List<ProductResponseDTO> responseDTOS = inventoryMapper.convertProductDaoListToDtoList(productsList);
        return BaseResponseUtility.getBaseResponse(responseDTOS);
    }

    @Override
    public BaseResponse addProducts(AddProductRequestDTO data) throws BadRequestException {
        Boolean isProductExists = inventoryRepository.existsByProductNameIgnoreCase(data.getProductName());
        if(Boolean.TRUE.equals(isProductExists)){
            throw new BadRequestException(ExceptionConstants.PRODUCT_ALREADY_EXISTS);
        }
        Products product = inventoryMapper.addProductDtoToProductDao(data);
        product.setCreatedAt(LocalDateTime.now());
        product = inventoryRepository.save(product);
        return BaseResponseUtility.getBaseResponse(product);
    }

    @Override
    public BaseResponse getByProductId(Long productId) throws BadRequestException {
        Products product = inventoryRepository.findByIdAndIsActiveTrue(productId);
        if(ObjectUtils.isEmpty(product)){
            throw new BadRequestException(ExceptionConstants.INVALID_PRODUCT_ID);
        }
        return BaseResponseUtility.getBaseResponse(product);
    }
}

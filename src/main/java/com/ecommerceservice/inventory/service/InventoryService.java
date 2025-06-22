package com.ecommerceservice.inventory.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.inventory.model.request.AddProductRequestDTO;

import java.util.List;

public interface InventoryService {
    BaseResponse getAllProducts();

    BaseResponse addProducts( AddProductRequestDTO dto) throws BadRequestException;

    BaseResponse getByProductId(Long productId) throws BadRequestException;
}

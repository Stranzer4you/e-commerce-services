package com.ecommerceservice.inventory.service;

import com.ecommerceservice.inventory.model.request.ProductCategoryDto;
import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.inventory.model.request.AddProductRequestDTO;
import com.ecommerceservice.inventory.model.request.UpdateProductRequestDto;

import java.util.List;

public interface InventoryService {
    BaseResponse getAllProducts(Integer categoryId,String searchText);

    BaseResponse addProducts( AddProductRequestDTO dto) throws BadRequestException;

    BaseResponse getByProductId(Long productId) throws BadRequestException;

    BaseResponse updateProductById(Long productId, UpdateProductRequestDto updateProductRequestDto) throws BadRequestException;

    BaseResponse updateProductStatus(Long productId, Boolean isActive) throws BadRequestException;

    BaseResponse addCategory(List<ProductCategoryDto> dto);

    BaseResponse getAllCategories();

    BaseResponse getProductsByCategoryId(Integer categoryId);
}

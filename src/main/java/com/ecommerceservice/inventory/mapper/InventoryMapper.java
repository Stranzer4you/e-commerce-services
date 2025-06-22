package com.ecommerceservice.inventory.mapper;

import com.ecommerceservice.inventory.dao.Products;
import com.ecommerceservice.inventory.model.request.AddProductRequestDTO;
import com.ecommerceservice.inventory.model.response.ProductResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    ProductResponseDTO convertDaoToDto(Products product);
    List<ProductResponseDTO> convertProductDaoListToDtoList(List<Products> productsList);

    Products addProductDtoToProductDao(AddProductRequestDTO data);
}

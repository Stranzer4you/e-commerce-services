package com.ecommerceservice.inventory.mapper;

import com.ecommerceservice.inventory.dao.Product;
import com.ecommerceservice.inventory.model.request.AddProductRequestDTO;
import com.ecommerceservice.inventory.model.request.UpdateProductRequestDto;
import com.ecommerceservice.inventory.model.response.ProductResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    ProductResponseDTO convertDaoToDto(Product product);
    List<ProductResponseDTO> convertProductDaoListToDtoList(List<Product> productList);

    Product addProductDtoToProductDao(AddProductRequestDTO data);

}

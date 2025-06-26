package com.ecommerceservice.inventory.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.config.BaseResponseUtility;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.inventory.dao.Product;
import com.ecommerceservice.inventory.mapper.InventoryMapper;
import com.ecommerceservice.inventory.model.request.AddProductRequestDTO;
import com.ecommerceservice.inventory.model.request.UpdateProductRequestDto;
import com.ecommerceservice.inventory.model.response.ProductResponseDTO;
import com.ecommerceservice.inventory.repository.InventoryRepository;
import com.ecommerceservice.utility.constants.ExceptionConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ecommerceservice.utility.constants.CommonConstants.ZERO_AVAILABLE_QUANTITY;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    public BaseResponse getAllProducts() {

        List<Product> productList = inventoryRepository.findAll();
        List<ProductResponseDTO> responseDTOS = inventoryMapper.convertProductDaoListToDtoList(productList);
        return BaseResponseUtility.getBaseResponse(responseDTOS);
    }

    @Override
    public BaseResponse addProducts(AddProductRequestDTO data) throws BadRequestException {
        Boolean isProductExists = inventoryRepository.existsByProductNameIgnoreCase(data.getProductName());
        if (Boolean.TRUE.equals(isProductExists)) {
            throw new BadRequestException(ExceptionConstants.PRODUCT_ALREADY_EXISTS);
        }
        Product product = inventoryMapper.addProductDtoToProductDao(data);
        product.setCreatedAt(LocalDateTime.now());
        product = inventoryRepository.save(product);
        return BaseResponseUtility.getBaseResponse(product);
    }

    @Override
    public BaseResponse getByProductId(Long productId) throws BadRequestException {
        Product product = fetchProductDetailsById(productId);
        return BaseResponseUtility.getBaseResponse(product);
    }

    @Override
    public BaseResponse updateProductById(Long productId, UpdateProductRequestDto updateProductRequestDto) throws BadRequestException {
        Product product = fetchProductDetailsById(productId);
        product.setProductName(updateProductRequestDto.getProductName());
        product.setPrice(updateProductRequestDto.getPrice());
        product.setIsActive(updateProductRequestDto.getIsActive());
        product.setQuantity(updateProductRequestDto.getQuantity());
        product.setIsAvailable(updateProductRequestDto.getIsAvailable());
        product = inventoryRepository.save(product);
        return BaseResponseUtility.getBaseResponse(product);
    }

    @Override
    public BaseResponse updateProductStatus(Long productId, Boolean isActive) throws BadRequestException {
        Product product = inventoryRepository.findById(productId).orElseThrow(() -> new BadRequestException(ExceptionConstants.INVALID_PRODUCT_ID));
        product.setIsActive(isActive);
        product = inventoryRepository.save(product);
        return BaseResponseUtility.getBaseResponse(product);
    }

    public Product fetchProductDetailsById(Long productId) throws BadRequestException {
        Product product = inventoryRepository.findByIdAndIsActiveTrue(productId);
        if (ObjectUtils.isEmpty(product)) {
            throw new BadRequestException(ExceptionConstants.INVALID_PRODUCT_ID);
        }
        return product;
    }

    public void updateProductQuantity(Long productId, Integer quantity) throws BadRequestException {
        Product product = fetchProductDetailsById(productId);
        Integer productQuantity = product.getQuantity();
        if (quantity > productQuantity) {
            throw new BadRequestException(ExceptionConstants.PRODUCT_QUANTITY_IS_LESS);
        }
        productQuantity = productQuantity - quantity;
        if (productQuantity.equals(ZERO_AVAILABLE_QUANTITY)) {
            product.setIsAvailable(false);
        }
        product.setQuantity(productQuantity);
        inventoryRepository.save(product);
    }
}

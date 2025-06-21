package com.ecommerceservice.inventory.service;

import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.config.BaseResponseUtility;
import com.ecommerceservice.inventory.dao.Products;
import com.ecommerceservice.inventory.mapper.InventoryMapper;
import com.ecommerceservice.inventory.model.ProductResponseDTO;
import com.ecommerceservice.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

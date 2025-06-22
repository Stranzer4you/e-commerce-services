package com.ecommerceservice.inventory.controller;


import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.inventory.model.request.AddProductRequestDTO;
import com.ecommerceservice.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/all-products")
    public BaseResponse getAllProducts(){
        return  inventoryService.getAllProducts();
    }

    @PostMapping("/add-products")
    public BaseResponse addProducts(@RequestBody @Valid  AddProductRequestDTO dto) throws BadRequestException {
        return inventoryService.addProducts(dto);
    }

    @GetMapping("/product/{productId}")
    public BaseResponse getByProductId(@PathVariable("productId") Long productId) throws BadRequestException {
        return  inventoryService.getByProductId(productId);
    }


}

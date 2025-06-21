package com.ecommerceservice.inventory.controller;


import com.ecommerceservice.config.BaseResponse;
import com.ecommerceservice.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/all-products")
    public BaseResponse getAllProducts(){
        return  inventoryService.getAllProducts();
    }


}

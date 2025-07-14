package com.ecommerceservice.customers.controller;

import com.ecommerceservice.customers.model.request.CartItemRequestDTO;
import com.ecommerceservice.customers.model.request.DeleteItemRequestDTO;
import com.ecommerceservice.customers.service.CustomerService;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.utility.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartItemController {

    @Autowired
    private CustomerService customerService;

    @GetMapping()
    public BaseResponse getCustomerCartDetails(@RequestParam("customerId") Long customerId){
        return customerService.getCustomerCartDetails(customerId);
    }
    @PostMapping()
    public BaseResponse addToCart(@RequestBody CartItemRequestDTO dto) throws BadRequestException {
        return customerService.addToCart(dto);
    }


    @PutMapping()
    public BaseResponse updateCart(@RequestBody List<CartItemRequestDTO> dto) throws BadRequestException {
        return  customerService.updateCartItem(dto);
    }

    @GetMapping("/count")
    public BaseResponse getCustomerCartCount(@RequestParam("customerId") Long customerId){
        return customerService.getCustomerCartCount(customerId);
    }

    @DeleteMapping()
    public BaseResponse removeFromCart(@RequestBody @Valid DeleteItemRequestDTO dto) throws BadRequestException {
        return customerService.removeFromCart(dto);
    }

    @DeleteMapping("/all")
    public BaseResponse clearCart(@RequestParam("customerId") Long customerId){
        return  customerService.clearCart(customerId);
    }
}

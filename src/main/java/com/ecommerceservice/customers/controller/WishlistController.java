package com.ecommerceservice.customers.controller;

import com.ecommerceservice.customers.model.request.DeleteItemRequestDTO;
import com.ecommerceservice.customers.service.CustomerService;
import com.ecommerceservice.exceptions.BadRequestException;
import com.ecommerceservice.utility.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {


    @Autowired
    private CustomerService customerService;

    @PostMapping()
    public BaseResponse addToWishlist(@RequestBody @Valid DeleteItemRequestDTO dto) throws BadRequestException {
         return  customerService.addToWishlist(dto);

    }

    @DeleteMapping()
    public BaseResponse removeFromWishlist(@RequestBody @Valid DeleteItemRequestDTO dto) throws BadRequestException {
        return customerService.removeFromWishlist(dto);
    }

    @GetMapping()
    public BaseResponse getWishlist(@RequestParam("customerId") Long customerId) {
        return customerService.getWishlist(customerId);
    }

    @GetMapping("/count")
    public BaseResponse getCustomerWishlistCount(@RequestParam("customerId") Long customerId){
        return  customerService.getWishlistCount(customerId);
    }

}

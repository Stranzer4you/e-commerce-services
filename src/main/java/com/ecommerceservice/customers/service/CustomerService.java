package com.ecommerceservice.customers.service;

import com.ecommerceservice.customers.model.request.CartItemRequestDTO;
import com.ecommerceservice.customers.model.request.DeleteItemRequestDTO;
import com.ecommerceservice.utility.BaseResponse;
import com.ecommerceservice.customers.model.request.AddCustomerRequest;
import com.ecommerceservice.exceptions.BadRequestException;

import java.util.List;

public interface CustomerService {
    BaseResponse getAllCustomers();

    BaseResponse addCustomer( AddCustomerRequest addCustomerRequest) throws BadRequestException;

    BaseResponse getCustomerById(Long customerId) throws BadRequestException;

    BaseResponse addToCart(CartItemRequestDTO dto) throws BadRequestException;

    BaseResponse updateCartItem(List<CartItemRequestDTO> dto) throws BadRequestException;

    BaseResponse getCustomerCartDetails(Long customerId);


    BaseResponse addToWishlist(DeleteItemRequestDTO dto) throws BadRequestException;

    BaseResponse removeFromWishlist(DeleteItemRequestDTO dto) throws BadRequestException;

    BaseResponse getWishlist(Long customerId);

    BaseResponse getCustomerCartCount(Long customerId);

    BaseResponse getWishlistCount(Long customerId);

    BaseResponse getWishlistCartCount(Long customerId);

    BaseResponse getCartDetails(Long customerId);

    BaseResponse removeFromCart( DeleteItemRequestDTO dto) throws BadRequestException;

    BaseResponse clearCart(Long customerId);
}

package com.ecommerceservice.customers.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistCartCountResponseDto {
    private Integer wishlistCount;
    private Integer cartCount;
}

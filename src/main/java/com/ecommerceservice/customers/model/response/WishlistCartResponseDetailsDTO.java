package com.ecommerceservice.customers.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistCartResponseDetailsDTO {
    List<WishlistResponseDTO> wishlistItems;
    List<CartResponseDTO> cartItems;
}

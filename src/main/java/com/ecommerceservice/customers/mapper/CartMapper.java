package com.ecommerceservice.customers.mapper;

import com.ecommerceservice.customers.dao.CartItemDao;
import com.ecommerceservice.customers.model.response.CartDetailsResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDetailsResponseDTO carDaoToDto(CartItemDao dao);

    List<CartDetailsResponseDTO> cartsDaoToCartsDto(List<CartItemDao> cartItemDao);
}

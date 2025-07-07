package com.ecommerceservice.customers.repository;

import com.ecommerceservice.customers.dao.CartItemDao;
import com.ecommerceservice.utility.BaseResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemDao,Long> {
    List<CartItemDao> findAllByProductIdInAndCustomerId(List<Long> list, Long customerId);


    List<CartItemDao> findAllByCustomerId(Long customerId);

    Long countByCustomerId(Long customerId);

    CartItemDao findByCustomerIdAndProductId(Long customerId, Long productId);
}

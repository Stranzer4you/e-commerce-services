package com.ecommerceservice.customers.repository;

import com.ecommerceservice.customers.dao.WishlistItemDao;
import com.ecommerceservice.utility.BaseResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface WishListRepository extends JpaRepository<WishlistItemDao,Long> {
    Optional<WishlistItemDao> findByCustomerIdAndProductId(Long customerId, Long productId);
    List<WishlistItemDao> findAllByCustomerId(Long customerId);
    void deleteByCustomerIdAndProductId(Long customerId, Long productId);
    Long countByCustomerId(Long customerId);
}

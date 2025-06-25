package com.ecommerceservice.inventory.repository;

import com.ecommerceservice.inventory.dao.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Products,Long> {
    Boolean existsByProductNameIgnoreCase(String productName);

    Products findByIdAndIsActiveTrue(Long productId);

   List<Products> findAllByIdInAndIsActiveTrue(List<Long> productIds);
}

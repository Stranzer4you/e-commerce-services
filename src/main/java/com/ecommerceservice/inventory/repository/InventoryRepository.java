package com.ecommerceservice.inventory.repository;

import com.ecommerceservice.inventory.dao.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Product,Long> {
    Boolean existsByProductNameIgnoreCase(String productName);

    Product findByIdAndIsActiveTrue(Long productId);

   List<Product> findAllByIdInAndIsActiveTrue(List<Long> productIds);
}

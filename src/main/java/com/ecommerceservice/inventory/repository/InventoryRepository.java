package com.ecommerceservice.inventory.repository;

import com.ecommerceservice.inventory.dao.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface InventoryRepository extends JpaRepository<Products,Long> {
    Boolean existsByProductNameIgnoreCase(String productName);
}

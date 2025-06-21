package com.ecommerceservice.inventory.repository;

import com.ecommerceservice.inventory.dao.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Products,Long> {
}

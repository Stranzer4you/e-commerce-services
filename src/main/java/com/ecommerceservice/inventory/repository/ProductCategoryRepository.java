package com.ecommerceservice.inventory.repository;

import com.ecommerceservice.inventory.dao.Product;
import com.ecommerceservice.inventory.dao.ProductCategoryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryDao , Long> {


    List<ProductCategoryDao> findAllByOrderByIdAsc();
}

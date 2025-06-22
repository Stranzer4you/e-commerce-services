package com.ecommerceservice.orders.repository;

import com.ecommerceservice.orders.dao.OrdersDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersDao,Long> {
}

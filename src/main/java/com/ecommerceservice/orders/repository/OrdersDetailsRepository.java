package com.ecommerceservice.orders.repository;

import com.ecommerceservice.orders.dao.OrdersDetailsDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersDetailsRepository extends JpaRepository<OrdersDetailsDao,Long> {
    List<OrdersDetailsDao> findAllByOrderId(Long orderId);
}

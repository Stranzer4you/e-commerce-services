package com.ecommerceservice.shipping.repository;

import com.ecommerceservice.shipping.dao.ShippingDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingDao,Long> {
    List<ShippingDao> findAllByStatusIn(List<Integer> notificationStatus);
}

package com.ecommerceservice.shipping.repository;

import com.ecommerceservice.shipping.dao.ShippingDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingDao,Long> {
    List<ShippingDao> findAllByStatusIn(List<Integer> notificationStatus);

    ShippingDao findByOrderIdAndCustomerIdAndStatus(Long orderId, Long customerId, Integer shippingStatusId);

    ShippingDao findByOrderId(Long orderId);
}

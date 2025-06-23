package com.ecommerceservice.payments.repository;

import com.ecommerceservice.payments.dao.PaymentDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentDao,Long> {
    List<PaymentDao> findAllByStatusIn(List<Integer> notificationStatus);
}

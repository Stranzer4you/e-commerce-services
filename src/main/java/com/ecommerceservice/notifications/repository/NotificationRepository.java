package com.ecommerceservice.notifications.repository;

import com.ecommerceservice.notifications.dao.NotificationDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationDao,Long> {
    List<NotificationDao> findAllByStatusIn(List<Integer> notificationStatus);
}

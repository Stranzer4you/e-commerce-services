package com.ecommerceservice.notifications.repository;

import com.ecommerceservice.notifications.dao.NotificationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationMessageRepository extends JpaRepository<NotificationMessage, Long> {

    @Query(value = """
            select "Template" from "NotificationMessage"
            where "NotificationModuleID" =:moduleId and "Status"=:statusId and "NotificationType"=:notificationType
            """,nativeQuery = true)
    String  findByNotificationModuleIdAndStatusAndNotificationType(@Param("moduleId") Integer moduleId,@Param("statusId") Integer statusId, @Param("notificationType") Integer notificationType);
}

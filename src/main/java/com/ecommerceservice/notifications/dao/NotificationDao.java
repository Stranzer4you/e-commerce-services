package com.ecommerceservice.notifications.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "\"Notification\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"CustomerID\"")
    private Long customerId;

    @Column(name = "\"OrderID\"")
    private Long orderId;


    @Column(name = "\"NotificationType\"")
    private Integer notificationType;

    @Column(name = "\"Status\"")
    private Integer status;

    @Column(name = "\"Message\"")
    private String message;

    @Column(name = "\"Error\"")
    private String error;

    @Column(name = "\"NotifyTime\"")
    private LocalDateTime createdAt;

    @Column(name = "\"NotificationModuleID\"")
    private Integer notificationModuleId;


}

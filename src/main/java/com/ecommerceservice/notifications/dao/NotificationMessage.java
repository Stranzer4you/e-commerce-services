package com.ecommerceservice.notifications.dao;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "NotificationMessage")
@Data
public class NotificationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"NotificationModuleID\"")
    private Integer notificationModuleId;

    @Column(name = "\"Status\"")
    private Integer status;

    @Column(name = "\"NotificationType\"")
    private String notificationType;

    @Column(name = "\"Template\"")
    private String template;
}

package com.ecommerceservice.orders.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"OrdersDetails\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDetailsDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"OrderID\"")
    private Long orderId;

    @Column(name = "\"ProductID\"")
    private Double productId;

    @Column(name = "\"Quantity\"")
    private Integer quantity;

    @Column(name = "\"AmountPaid\"")
    private Double amountPaid;
}

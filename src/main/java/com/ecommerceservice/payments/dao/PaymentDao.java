package com.ecommerceservice.payments.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"Payments\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"CustomerID\"")
    private Long customerId;

    @Column(name = "\"Amount\"")
    private Double amount;

    @Column(name = "\"OrderID\"")
    private Long orderId;

    @Column(name = "\"Status\"")
    private Integer status;

    @Column(name = "\"PaymentTime\"")
    private LocalDateTime paymentTime;


}

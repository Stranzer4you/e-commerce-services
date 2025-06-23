package com.ecommerceservice.shipping.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"Shipping\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"CustomerID\"")
    private Long customerId;

    @Column(name = "\"OrderID\"")
    private Long orderId;

    @Column(name = "\"Address\"")
    private String address;

    @Column(name = "\"Status\"")
    private Integer status;

    @Column(name = "\"ShippedAt\"")
    private LocalDateTime shippedAt;


}

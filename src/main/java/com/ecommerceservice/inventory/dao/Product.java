package com.ecommerceservice.inventory.dao;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"Products\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"ProductName\"")
    private String productName;

    @Column(name = "\"Price\"")
    private Double price;

    @Column(name = "\"Quantity\"")
    private Integer quantity;

    @Column(name = "\"IsAvailable\"")
    private Boolean isAvailable;

    @Column(name = "\"CreatedAt\"")
    private LocalDateTime createdAt;

    @Column(name = "\"IsActive\"")
    private Boolean isActive;


}

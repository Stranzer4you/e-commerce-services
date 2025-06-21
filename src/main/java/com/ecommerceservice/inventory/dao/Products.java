package com.ecommerceservice.inventory.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "\"Products\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products {

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
    private LocalTime createdAt;

}

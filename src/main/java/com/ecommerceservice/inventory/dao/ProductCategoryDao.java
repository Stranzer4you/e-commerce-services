package com.ecommerceservice.inventory.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "\"ProductCategory\"")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"Name\"")
    private String productName;

    @Column(name = "\"CreatedAt\"")
    private LocalDateTime createdAt;

    @Column(name = "\"IsActive\"")
    private Boolean isActive;

}

package com.ecommerceservice.customers.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "\"WishlistItem\"")
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class WishlistItemDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"CustomerID\"")
    private Long customerId;

    @Column(name = "\"ProductID\"")
    private Long productId;

    @Column(name = "\"CreatedAt\"")
    private LocalDateTime createdAt;

}

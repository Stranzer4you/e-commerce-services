package com.ecommerceservice.orders.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "\"Orders\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"CustomerID\"")
    private Long customerId;

    @Column(name = "\"TotalAmount\"")
    private Double totalAmount;

    @Column(name = "\"Status\"")
    private Integer status;

    @Column(name = "\"CreatedAt\"")
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "\"OrderID\"",referencedColumnName = "\"ID\"")
    private List<OrdersDetailsDao> ordersDetailsDaoList;

}

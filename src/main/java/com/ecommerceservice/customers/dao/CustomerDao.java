package com.ecommerceservice.customers.dao;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "\"Customers\"")
public class CustomerDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"CustomerName\"")
    private String customerName;

    @Column(name = "\"PhoneNumber\"")
    private String phoneNumber;

    @Column(name = "\"Email\"")
    private String email;

    @Column(name = "\"Address\"")
    private String address;

    @Column(name = "\"IsActive\"")
    private Boolean isActive=true;


}

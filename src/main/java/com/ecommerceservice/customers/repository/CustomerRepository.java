package com.ecommerceservice.customers.repository;

import com.ecommerceservice.customers.dao.CustomerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerDao , Long> {
    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);
}

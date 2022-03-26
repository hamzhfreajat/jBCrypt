package com.example.jBCrypt.infraStructure;

import com.example.jBCrypt.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer , Long> {
    Customer findCustomerByUsername(String username);
}

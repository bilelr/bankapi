package com.github.bilalr.bankapi.repository;

import com.github.bilalr.bankapi.model.Customer;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {
    private final JdbcClient jdbcClient;

    public CustomerRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Customer> getAllCustomers() {
        return jdbcClient.sql("SELECT * FROM Customer")
                .query(Customer.class)
                .list();
    }

    public Optional<Customer> getCustomerById(Integer customerId) {
        return jdbcClient.sql("SELECT * FROM Customer WHERE id = :customerId")
                .param("customerId", customerId)
                .query(Customer.class)
                .optional();
    }
}

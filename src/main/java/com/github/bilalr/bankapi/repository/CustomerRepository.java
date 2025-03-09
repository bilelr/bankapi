package com.github.bilalr.bankapi.repository;

import com.github.bilalr.bankapi.model.Customer;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepository {
    private final JdbcClient jdbcClient;

    public CustomerRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Customer> getAllCustomers() {
        return jdbcClient.sql("SELECT * from Customer")
                .query(Customer.class)
                .list();
    }
}

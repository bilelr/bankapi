package com.github.bilalr.bankapi.controller;

import com.github.bilalr.bankapi.model.Customer;
import com.github.bilalr.bankapi.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerRepository customerRepository;

    @Test
    void testGetAllCustomers() throws Exception {
        List<Customer> customers = List.of(
                new Customer(1, "John Smith"),
                new Customer(2, "Alan Turing"),
                new Customer(3, "James Bond")
        );

        when(customerRepository.getAllCustomers()).thenReturn(customers);

        this.mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                    {"id":1,"name":"John Smith"},
                                    {"id":2,"name":"Alan Turing"},
                                    {"id":3,"name":"James Bond"}
                                   ]"""));
    }
}

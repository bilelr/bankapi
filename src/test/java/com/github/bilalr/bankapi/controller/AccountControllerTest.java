package com.github.bilalr.bankapi.controller;

import com.github.bilalr.bankapi.model.Account;
import com.github.bilalr.bankapi.model.Balance;
import com.github.bilalr.bankapi.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @Test
    void testGetAccountBalance() throws Exception {
        when(accountService.getAccountBalance(1, 1)).thenReturn(
                Optional.of(new Balance(BigDecimal.TEN, "EUR"))
        );

        this.mockMvc.perform(get("/api/customers/1/accounts/1/balance"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"balance":10,"currency":"EUR"}"""));
    }

    @Test
    void testCreateAccount() throws Exception {
        Account account = new Account(null, 1, new BigDecimal("211.11"),
                "DZD");

        this.mockMvc.perform(post("/api/customers/1/accounts")
                        .content("""
                                {
                                  "customerId": 1,
                                  "balance": 211.11,
                                  "currency": "DZD"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Mockito.verify(accountService, times(1)).createAccount(account);
    }

    @Test
    void testGetAllCustomerAccounts() throws Exception {
        List<Account> accounts = List.of(
                new Account(1, 1, BigDecimal.ONE, "EUR"),
                new Account(2, 1, BigDecimal.TEN, "DZD"),
                new Account(3, 1, BigDecimal.ZERO, "USD")
        );

        when(accountService.getAllCustomerAccounts(1)).thenReturn(accounts);

        this.mockMvc.perform(get("/api/customers/1/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id":1,"customerId":1,"balance":1,"currency":"EUR"},
                                {"id":2,"customerId":1,"balance":10,"currency":"DZD"},
                                {"id":3,"customerId":1,"balance":0,"currency":"USD"}]
                                """));
    }

}

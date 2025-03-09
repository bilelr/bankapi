package com.github.bilalr.bankapi.controller;

import com.github.bilalr.bankapi.exception.AccountNotFoundException;
import com.github.bilalr.bankapi.model.Account;
import com.github.bilalr.bankapi.model.Balance;
import com.github.bilalr.bankapi.repository.AccountRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class exposes the end points for creating, retrieving, and updating accounts.
 * Account is a sub-resource of the customer resource.
 * Each account belongs to one customer.
 * A customer can have multiple accounts.
 */
@RestController
@RequestMapping("/api/customers/{customerId}/accounts")
public class AccountController {
    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createAccount(@Valid @RequestBody Account account) {
        accountRepository.createAccount(account);
    }

    @GetMapping("/{accountId}/balance")
    public Balance getAccountBalance(@PathVariable Integer customerId,
                                     @PathVariable Integer accountId) {
        return accountRepository.getAccountBalance(customerId, accountId)
                .orElseThrow(AccountNotFoundException::new);
    }

    @GetMapping
    public List<Account> getAllCustomerAccounts(@PathVariable Integer customerId) {
        return accountRepository.getAllCustomerAccounts(customerId);
    }
}

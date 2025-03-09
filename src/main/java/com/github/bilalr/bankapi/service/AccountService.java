package com.github.bilalr.bankapi.service;

import com.github.bilalr.bankapi.exception.CustomerNotFoundException;
import com.github.bilalr.bankapi.model.Account;
import com.github.bilalr.bankapi.model.Balance;
import com.github.bilalr.bankapi.model.Customer;
import com.github.bilalr.bankapi.repository.AccountRepository;
import com.github.bilalr.bankapi.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This service class handles the business logic for creating, retrieving, and updating accounts.
 */
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    public void createAccount(Account account) {
        Optional<Customer> customer = customerRepository.getCustomerById(account.customerId());

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        accountRepository.createAccount(account);
    }

    public Optional<Balance> getAccountBalance(Integer customerId, Integer accountId) {
        return accountRepository.getAccountBalance(customerId, accountId);
    }

    public List<Account> getAllCustomerAccounts(Integer customerId) {
        return accountRepository.getAllCustomerAccounts(customerId);
    }
}

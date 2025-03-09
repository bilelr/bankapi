package com.github.bilalr.bankapi.repository;

import com.github.bilalr.bankapi.model.Account;
import com.github.bilalr.bankapi.model.Balance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepository {
    private final Logger logger = LoggerFactory.getLogger(AccountRepository.class);
    private final JdbcClient jdbcClient;

    public AccountRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void createAccount(Account account) {
        logger.debug("Creating account {}", account);
        jdbcClient.sql(
                        "INSERT INTO Account (customer_id, balance, currency) values (?, ?, ?)")
                .params(account.customerId(), account.balance(), account.currency())
                .update();
    }

    public Optional<Balance> getAccountBalance(Integer customerId, Integer accountId) {
        return jdbcClient.sql(
                        "SELECT balance, currency FROM Account WHERE id = :accountId AND customer_id = :customerId")
                .param("accountId", accountId)
                .param("customerId", customerId)
                .query(Balance.class)
                .optional();
    }

    public List<Account> getAllCustomerAccounts(Integer customerId) {
        return jdbcClient.sql("SELECT * FROM Account WHERE customer_id = :customerId")
                .param("customerId", customerId)
                .query(Account.class)
                .list();
    }

    public Optional<Account> getAccountById(Integer customerId, Integer accountId) {
        return jdbcClient.sql("SELECT * FROM Account WHERE id = :accountId AND customer_id = :customerId")
                .param("accountId", accountId)
                .param("customerId", customerId)
                .query(Account.class)
                .optional();
    }

    /**
     * Update the balance of the given account to the specified amount.
     *
     * @param account the account to update.
     * @param balance the amount to set.
     */
    public void updateBalance(Account account, BigDecimal balance) {
        jdbcClient.sql("UPDATE Account SET balance = :balance WHERE id = :accountId")
                .param("balance", balance)
                .param("accountId", account.id())
                .update();
    }
}

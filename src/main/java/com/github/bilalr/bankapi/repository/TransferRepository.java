package com.github.bilalr.bankapi.repository;

import com.github.bilalr.bankapi.model.Transfer;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransferRepository {
    private final JdbcClient jdbcClient;

    public TransferRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void createTransfer(Transfer transfer) {
        jdbcClient.sql(
                        "INSERT INTO transfer (from_customer_id, from_account_id," +
                                "to_customer_id, to_account_id, amount) values (?, ?, ?, ?, ?)")
                .params(transfer.fromCustomerId(), transfer.fromAccountId(),
                        transfer.toCustomerId(), transfer.toAccountId(), transfer.amount())
                .update();
    }

    public List<Transfer> getTransfersForAccount(Integer customerId, Integer accountId) {
        return jdbcClient.sql(
                        "SELECT * FROM transfer WHERE (from_account_id = :accountId AND from_customer_id = :customerId) " +
                                "OR (to_account_id = :accountId AND to_customer_id = :customerId) ORDER BY ID")
                .param("accountId", accountId)
                .param("customerId", customerId)
                .query(Transfer.class)
                .list();
    }
}

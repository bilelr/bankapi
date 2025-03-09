package com.github.bilalr.bankapi.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * A record representing the transfer resource.  A transfer is created when funds need to be moved
 * from one account to another.
 * The FROM and TO account must have the same currency.
 * The balance of the FROM account must be greater than or equal to the amount being transferred.
 * @param id the id of the transfer.
 * @param fromCustomerId the id of the customer who owns the account the money is transferred from.
 * @param fromAccountId the id of the account from which the money is transferred.
 * @param toCustomerId the id of the customer who owns the account to which the money is transferred.
 * @param toAccountId the id of the account to which the money is transferred.
 * @param amount the amount of money to transfer.
 */
public record Transfer(
        Integer id,
        @NotNull
        Integer fromCustomerId,
        @NotNull
        Integer fromAccountId,
        @NotNull
        Integer toCustomerId,
        @NotNull
        Integer toAccountId,
        @DecimalMin(value = "0.01")
        @Digits(integer = 38, fraction = 2)
        BigDecimal amount) {
}

package com.github.bilalr.bankapi.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
 * This record represents the Account resource.
 * @param id the id of the account i.e. the account number.
 * @param customerId the id of the customer to which this account belongs.
 * @param balance the current balance of the account.
 * @param currency the currency of this account in ISO 4217 3-letter format.
 */
public record Account(
        Integer id,
        @NotNull
        Integer customerId,
        @DecimalMin(value = "0.0")
        @Digits(integer = 38, fraction = 2)
        BigDecimal balance,
        @Length(min = 3, max = 3)
        String currency
) {
}

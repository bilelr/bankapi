package com.github.bilalr.bankapi.model;

import java.math.BigDecimal;

/**
 * A record representing a balance consisting of an amount and currency.
 * @param balance the amount of money.
 * @param currency the currency in ISO 4217 3-letter format.
 */
public record Balance(BigDecimal balance, String currency) {
}

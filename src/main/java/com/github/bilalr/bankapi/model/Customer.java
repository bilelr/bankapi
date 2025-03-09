package com.github.bilalr.bankapi.model;

import jakarta.validation.constraints.NotEmpty;

/**
 * A record representing a Customer resource.
 * @param id the id of the customer.
 * @param name the name of the customer.
 */
public record Customer(
        Integer id,
        @NotEmpty
        String name) {
}

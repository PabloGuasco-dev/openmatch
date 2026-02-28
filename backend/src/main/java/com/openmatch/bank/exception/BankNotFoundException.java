package com.openmatch.bank.exception;

/**
 * Exception when a banking entity is not found by id.
 */
public class BankNotFoundException extends RuntimeException {

    public BankNotFoundException(Long id) {
        super("Bank not found with id: " + id);
    }
}

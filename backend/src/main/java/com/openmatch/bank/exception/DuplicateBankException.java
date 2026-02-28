package com.openmatch.bank.exception;

/**
 * Exception when trying to create a bank with an already existing code.
 */
public class DuplicateBankException extends RuntimeException {

    public DuplicateBankException(String code) {
        super("A bank with the code already exists: " + code);
    }
}

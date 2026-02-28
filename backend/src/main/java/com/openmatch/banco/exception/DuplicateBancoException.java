package com.openmatch.banco.exception;

/**
 * Excepción cuando se intenta crear un banco con un código ya existente.
 */
public class DuplicateBancoException extends RuntimeException {

    public DuplicateBancoException(String codigo) {
        super("Ya existe un banco con el código: " + codigo);
    }
}

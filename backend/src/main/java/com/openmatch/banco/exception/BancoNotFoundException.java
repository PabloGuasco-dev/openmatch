package com.openmatch.banco.exception;

/**
 * Excepción cuando no se encuentra una entidad bancaria por id.
 */
public class BancoNotFoundException extends RuntimeException {

    public BancoNotFoundException(Long id) {
        super("Banco no encontrado con id: " + id);
    }
}

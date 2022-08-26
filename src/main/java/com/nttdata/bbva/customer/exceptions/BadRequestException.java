package com.nttdata.bbva.customer.exceptions;

public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public BadRequestException(String mensaje){
        super(mensaje);
    }
}

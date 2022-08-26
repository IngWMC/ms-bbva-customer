package com.nttdata.bbva.customer.exceptions;

public class ModelNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public ModelNotFoundException(String mensaje){
        super(mensaje);
    }
}

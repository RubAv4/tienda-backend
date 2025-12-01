package com.tienda.tiendaelectronica.excepciones;

public class ValidacionException extends RuntimeException {

    public ValidacionException() {
        super();
    }

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}

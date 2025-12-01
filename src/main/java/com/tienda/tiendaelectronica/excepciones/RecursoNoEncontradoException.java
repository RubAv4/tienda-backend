package com.tienda.tiendaelectronica.excepciones;

public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException() {
        super();
    }

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

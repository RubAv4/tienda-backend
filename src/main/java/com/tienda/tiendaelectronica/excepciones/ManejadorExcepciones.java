package com.tienda.tiendaelectronica.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("mensaje", ex.getMessage());
        cuerpo.put("fecha", LocalDateTime.now());
        cuerpo.put("estado", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo);
    }

    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(ValidacionException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("mensaje", ex.getMessage());
        cuerpo.put("fecha", LocalDateTime.now());
        cuerpo.put("estado", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo);
    }

    // Errores de @Valid en los controladores
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresValidacion(MethodArgumentNotValidException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("fecha", LocalDateTime.now());
        cuerpo.put("estado", HttpStatus.BAD_REQUEST.value());

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );

        cuerpo.put("errores", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo);
    }

    // Cualquier otro error no controlado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarGenerico(Exception ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("mensaje", "Error interno del servidor");
        cuerpo.put("detalle", ex.getMessage());
        cuerpo.put("fecha", LocalDateTime.now());
        cuerpo.put("estado", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cuerpo);
    }
}

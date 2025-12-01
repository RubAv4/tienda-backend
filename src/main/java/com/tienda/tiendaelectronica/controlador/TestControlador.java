package com.tienda.tiendaelectronica.controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:4200")
public class TestControlador {

    @GetMapping("/publico")
    public ResponseEntity<String> publico() {
        return ResponseEntity.ok("Endpoint público OK");
    }

    @GetMapping("/user")
    public ResponseEntity<String> soloUser() {
        return ResponseEntity.ok("Endpoint para usuarios autenticados (ROLE_USER)");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> soloAdmin() {
        return ResponseEntity.ok("Endpoint para administradores (ROLE_ADMIN)");
    }
}

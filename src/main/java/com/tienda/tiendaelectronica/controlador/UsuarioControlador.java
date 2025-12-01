package com.tienda.tiendaelectronica.controlador;

import com.tienda.tiendaelectronica.modelo.Usuario;
import com.tienda.tiendaelectronica.servicio.UsuarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// src/main/java/com/tienda/tiendaelectronica/controlador/UsuarioControlador.java

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    @GetMapping("/buscar")
    public ResponseEntity<Usuario> buscarPorUsername(@RequestParam String username) {
        Usuario usuario = usuarioServicio.obtenerPorUsername(username);
        return ResponseEntity.ok(usuario);
    }
}


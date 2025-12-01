package com.tienda.tiendaelectronica.controlador;

import com.tienda.tiendaelectronica.modelo.Usuario;
import com.tienda.tiendaelectronica.servicio.SeguridadServicio;
import com.tienda.tiendaelectronica.servicio.UsuarioServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthControlador {

    private final UsuarioServicio usuarioServicio;
    private final SeguridadServicio seguridadServicio;

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody Usuario usuario) {
        Usuario nuevo = usuarioServicio.registrarUsuario(usuario);
        return ResponseEntity.ok(nuevo);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        Usuario usuario = seguridadServicio.validarUsuario(
                request.username(),
                request.password()
        );

        String token = seguridadServicio.generarToken(usuario);

        return ResponseEntity.ok(new LoginResponse(token, usuario.getUsername()));
    }

    // records internos
    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String token, String username) {}
}

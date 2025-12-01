package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.configuracion.JwtUtilidad;
import com.tienda.tiendaelectronica.modelo.Usuario;
import com.tienda.tiendaelectronica.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.tienda.tiendaelectronica.excepciones.RecursoNoEncontradoException;

@Service
@RequiredArgsConstructor
public class SeguridadServicioImpl implements SeguridadServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilidad jwtUtilidad;

    @Override
    public String generarToken(Usuario usuario) {
        return jwtUtilidad.generarToken(usuario);
    }

    @Override
    public Long obtenerUsuarioActualId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No hay usuario autenticado");
        }

        String username = auth.getName();

        return usuarioRepositorio.findByUsername(username)
                .map(Usuario::getId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + username));
    }

    @Override
    public Usuario validarUsuario(String username, String password) {

        Usuario usuario = usuarioRepositorio.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String passwordBD = usuario.getPassword();

        // Si la contraseña en BD parece BCrypt (empieza con $2a$ o $2b$), usar matches
        if (passwordBD.startsWith("$2a$") || passwordBD.startsWith("$2b$")) {
            if (!passwordEncoder.matches(password, passwordBD)) {
                throw new RuntimeException("Credenciales incorrectas");
            }
        } else {
            // Usuario "sembrado" en la BD con contraseña en texto plano
            if (!passwordBD.equals(password)) {
                throw new RuntimeException("Credenciales incorrectas");
            }
        }

        return usuario;
    }

}

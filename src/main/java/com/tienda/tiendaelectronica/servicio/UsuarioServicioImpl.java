package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.excepciones.RecursoNoEncontradoException;
import com.tienda.tiendaelectronica.modelo.Usuario;
import com.tienda.tiendaelectronica.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServicioImpl implements UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        // aquí puedes setear valores por defecto
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEnabled(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        return usuarioRepositorio.save(usuario);
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
    }

    @Override
    public Usuario obtenerPorUsername(String username) {
        return usuarioRepositorio.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + username));
    }
}

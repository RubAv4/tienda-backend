package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.modelo.Usuario;

import java.util.List;

public interface UsuarioServicio {

    List<Usuario> listarTodos();

    Usuario registrarUsuario(Usuario usuario);

    Usuario obtenerPorId(Long id);

    Usuario obtenerPorUsername(String username);
}

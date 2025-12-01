package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.modelo.Usuario;

public interface SeguridadServicio {

    String generarToken(Usuario usuario);

    Usuario validarUsuario(String username, String password);

    Long obtenerUsuarioActualId();

}

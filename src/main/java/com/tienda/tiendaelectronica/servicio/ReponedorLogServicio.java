package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.modelo.Producto;
import com.tienda.tiendaelectronica.modelo.ReponedorLog;
import com.tienda.tiendaelectronica.modelo.Usuario;

import java.util.List;

public interface ReponedorLogServicio {

    void registrarMovimiento(Usuario reponedor, Producto producto, int cantidad, String mensaje);

    List<ReponedorLog> listarTodos();
}

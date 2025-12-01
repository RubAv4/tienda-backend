package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.modelo.LogReponedor;

import java.util.List;

public interface LogReponedorServicio {

    List<LogReponedor> listarTodos();

    void registrarAccion(String username,
                         Long productoId,
                         Integer cantidadAgregada,
                         String mensaje);
}

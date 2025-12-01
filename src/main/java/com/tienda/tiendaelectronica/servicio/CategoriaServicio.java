/* src/main/java/com/tienda/tiendaelectronica/servicio/CategoriaServicio.java */
package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.modelo.Categoria;

import java.util.List;

public interface CategoriaServicio {

    List<Categoria> listarTodas();

    List<Categoria> listarActivas();

    Categoria obtenerPorId(Long id);

    Categoria crear(Categoria categoria);

    Categoria actualizar(Long id, Categoria datos);

    void desactivar(Long id);
}

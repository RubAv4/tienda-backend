package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.modelo.Producto;

import java.util.List;

public interface ProductoServicio {

    List<Producto> listarTodos();
    List<Producto> listarActivos();
    List<Producto> listarPorCategoria(Long categoriaId);
    List<Producto> buscarPorNombre(String nombre);
    Producto obtenerPorId(Long id);
    Producto crear(Producto producto);
    Producto actualizar(Long id, Producto datos);
    void eliminarLogico(Long id);

    // NUEVOS
    Producto actualizarStock(Long id, Integer nuevoStock);
    Producto cambiarEstadoActivo(Long id, boolean activo);
}

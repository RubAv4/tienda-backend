// src/main/java/com/tienda/tiendaelectronica/servicio/ProductoServicioImpl.java
package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.excepciones.RecursoNoEncontradoException;
import com.tienda.tiendaelectronica.modelo.Categoria;
import com.tienda.tiendaelectronica.modelo.Producto;
import com.tienda.tiendaelectronica.repositorio.CategoriaRepositorio;
import com.tienda.tiendaelectronica.repositorio.ProductoRepositorio;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServicioImpl implements ProductoServicio {

    private final ProductoRepositorio productoRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;

    @Override
    public List<Producto> listarTodos() {
        return productoRepositorio.findAll();
    }

    @Override
    public List<Producto> listarActivos() {
        return productoRepositorio.findByActivoTrue();
    }

    @Override
    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepositorio.findByCategoria_Id(categoriaId);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepositorio.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return productoRepositorio.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con id: " + id));
    }

    @Override
    public Producto crear(Producto producto) {

        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepositorio.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        producto.setActivo(true);
        return productoRepositorio.save(producto);
    }

    @Override
    public Producto actualizar(Long id, Producto datos) {
        Producto existente = obtenerPorId(id);

        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());
        existente.setImagenUrl(datos.getImagenUrl());
        existente.setActivo(datos.getActivo());

        if (datos.getCategoria() != null && datos.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepositorio.findById(datos.getCategoria().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada"));
            existente.setCategoria(categoria);
        }

        return productoRepositorio.save(existente);
    }

    @Override
    public void eliminarLogico(Long id) {
        Producto producto = obtenerPorId(id);
        producto.setActivo(false);
        productoRepositorio.save(producto);
    }

    // 👇 ADMIN + REPONEDOR: actualizar stock
    @Override
    public Producto actualizarStock(Long id, Integer nuevoStock) {
        Producto producto = obtenerPorId(id);
        producto.setStock(nuevoStock);
        return productoRepositorio.save(producto);
    }

    // 👇 ADMIN + REPONEDOR: marcar disponible / agotado
    @Override
    public Producto cambiarEstadoActivo(Long id, boolean activo) {
        Producto producto = obtenerPorId(id);
        producto.setActivo(activo);
        return productoRepositorio.save(producto);
    }
}

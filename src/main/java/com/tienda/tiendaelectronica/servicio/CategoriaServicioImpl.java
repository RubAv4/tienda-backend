/* src/main/java/com/tienda/tiendaelectronica/servicio/CategoriaServicioImpl.java */
package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.excepciones.RecursoNoEncontradoException;
import com.tienda.tiendaelectronica.modelo.Categoria;
import com.tienda.tiendaelectronica.repositorio.CategoriaRepositorio;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaServicioImpl implements CategoriaServicio {

    private final CategoriaRepositorio categoriaRepositorio;

    @Override
    public List<Categoria> listarTodas() {
        return categoriaRepositorio.findAll();
    }

    @Override
    public List<Categoria> listarActivas() {
        // Método actualizado según el nuevo repositorio
        return categoriaRepositorio.findByActivoTrueOrderByNombreAsc();
    }

    @Override
    public Categoria obtenerPorId(Long id) {
        Optional<Categoria> opt = categoriaRepositorio.findById(id);
        if (opt.isEmpty()) {
            throw new RecursoNoEncontradoException("Categoría no encontrada con id: " + id);
        }
        return opt.get();
    }

    @Override
    public Categoria crear(Categoria categoria) {
        // Normalizar nombre y asegurar activo
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }

        String nombreNormalizado = categoria.getNombre().trim();

        // Evitar duplicados por nombre (case-insensitive)
        categoriaRepositorio.findByNombreIgnoreCase(nombreNormalizado)
                .ifPresent(existente -> {
                    throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
                });

        categoria.setNombre(nombreNormalizado);

        if (categoria.getActivo() == null) {
            categoria.setActivo(true);
        }

        return categoriaRepositorio.save(categoria);
    }

    @Override
    public Categoria actualizar(Long id, Categoria datos) {
        Optional<Categoria> opt = categoriaRepositorio.findById(id);
        if (opt.isEmpty()) {
            throw new RecursoNoEncontradoException("Categoría no encontrada con id: " + id);
        }

        Categoria existente = opt.get();

        if (datos.getNombre() == null || datos.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }

        String nombreNormalizado = datos.getNombre().trim();

        // Comprobar que no exista otra categoría con el mismo nombre
        categoriaRepositorio.findByNombreIgnoreCase(nombreNormalizado)
                .ifPresent(otra -> {
                    if (!otra.getId().equals(id)) {
                        throw new IllegalArgumentException("Ya existe otra categoría con ese nombre");
                    }
                });

        existente.setNombre(nombreNormalizado);
        existente.setDescripcion(datos.getDescripcion());
        existente.setActivo(datos.getActivo() != null ? datos.getActivo() : existente.getActivo());

        return categoriaRepositorio.save(existente);
    }

    @Override
    public void desactivar(Long id) {
        Optional<Categoria> opt = categoriaRepositorio.findById(id);
        if (opt.isEmpty()) {
            throw new RecursoNoEncontradoException("Categoría no encontrada con id: " + id);
        }

        Categoria categoria = opt.get();
        categoria.setActivo(false);
        categoriaRepositorio.save(categoria);
    }
}

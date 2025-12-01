// src/main/java/com/tienda/tiendaelectronica/repositorio/CategoriaRepositorio.java
package com.tienda.tiendaelectronica.repositorio;

import com.tienda.tiendaelectronica.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {

    // Para combos y listados ordenados
    List<Categoria> findByActivoTrueOrderByNombreAsc();

    // Para validar duplicados por nombre (case-insensitive)
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
}

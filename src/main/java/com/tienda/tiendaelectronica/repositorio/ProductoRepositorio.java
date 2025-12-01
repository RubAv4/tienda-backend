package com.tienda.tiendaelectronica.repositorio;

import com.tienda.tiendaelectronica.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepositorio extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();

    List<Producto> findByCategoria_Id(Long categoriaId);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}

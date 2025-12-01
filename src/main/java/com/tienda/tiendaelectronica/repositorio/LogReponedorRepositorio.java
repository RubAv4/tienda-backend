package com.tienda.tiendaelectronica.repositorio;

import com.tienda.tiendaelectronica.modelo.LogReponedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogReponedorRepositorio extends JpaRepository<LogReponedor, Long> {

    List<LogReponedor> findAllByOrderByFechaDesc();
}

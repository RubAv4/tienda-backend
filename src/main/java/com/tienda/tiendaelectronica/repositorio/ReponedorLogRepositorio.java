package com.tienda.tiendaelectronica.repositorio;

import com.tienda.tiendaelectronica.modelo.ReponedorLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReponedorLogRepositorio extends JpaRepository<ReponedorLog, Long> {

    List<ReponedorLog> findAllByOrderByFechaDesc();
}

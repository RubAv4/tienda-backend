package com.tienda.tiendaelectronica.repositorio;

import com.tienda.tiendaelectronica.modelo.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByOrderByFechaDesc();

    List<Pedido> findByUsuario_IdOrderByFechaDesc(Long usuarioId);
}

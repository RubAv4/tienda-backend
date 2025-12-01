package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.dto.ResumenVentaUsuarioDto;
import com.tienda.tiendaelectronica.modelo.Carrito;
import com.tienda.tiendaelectronica.modelo.Pedido;

import java.util.List;

public interface PedidoServicio {

    List<Pedido> listarTodos();

    List<Pedido> listarPorUsuario(Long usuarioId);

    Pedido registrarDesdeCarrito(Carrito carrito);

    List<ResumenVentaUsuarioDto> resumenVentasPorUsuario();
}

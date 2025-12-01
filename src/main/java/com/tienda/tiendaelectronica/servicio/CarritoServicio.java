/* src/main/java/com/tienda/tiendaelectronica/servicio/CarritoServicio.java */
package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.modelo.Carrito;
import com.tienda.tiendaelectronica.modelo.Pedido;

public interface CarritoServicio {

    Carrito obtenerCarritoAbiertoPorUsuario(Long usuarioId);

    Carrito agregarProducto(Long usuarioId, Long productoId, int cantidad);

    Carrito actualizarCantidad(Long usuarioId, Long productoId, int cantidad);

    Carrito vaciarCarrito(Long usuarioId);

    // devuelve el Pedido generado
    Pedido comprar(Long usuarioId);
}

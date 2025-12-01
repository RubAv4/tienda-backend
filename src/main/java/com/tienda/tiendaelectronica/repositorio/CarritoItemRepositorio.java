/* src/main/java/com/tienda/tiendaelectronica/repositorio/CarritoItemRepositorio.java */
package com.tienda.tiendaelectronica.repositorio;

import com.tienda.tiendaelectronica.modelo.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoItemRepositorio extends JpaRepository<CarritoItem, Long> {

    // item concreto dentro de un carrito (para sumar / cambiar cantidad)
    Optional<CarritoItem> findByCarrito_IdAndProducto_Id(Long carritoId, Long productoId);

    // todos los items de un carrito (para vaciar)
    List<CarritoItem> findByCarrito_Id(Long carritoId);
}

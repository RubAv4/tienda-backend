/* src/main/java/com/tienda/tiendaelectronica/repositorio/CarritoRepositorio.java */
package com.tienda.tiendaelectronica.repositorio;

import com.tienda.tiendaelectronica.modelo.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepositorio extends JpaRepository<Carrito, Long> {

    // por si lo usas en algún otro sitio
    Optional<Carrito> findByUsuario_Id(Long usuarioId);

    // usado por CarritoServicioImpl para obtener el carrito ABIERTO
    Optional<Carrito> findByUsuario_IdAndEstado(Long usuarioId, String estado);
}

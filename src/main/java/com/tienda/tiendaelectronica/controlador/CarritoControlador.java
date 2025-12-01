/* src/main/java/com/tienda/tiendaelectronica/controlador/CarritoControlador.java */
package com.tienda.tiendaelectronica.controlador;

import com.tienda.tiendaelectronica.excepciones.RecursoNoEncontradoException;
import com.tienda.tiendaelectronica.modelo.Carrito;
import com.tienda.tiendaelectronica.modelo.Pedido;
import com.tienda.tiendaelectronica.modelo.Usuario;
import com.tienda.tiendaelectronica.repositorio.UsuarioRepositorio;
import com.tienda.tiendaelectronica.servicio.CarritoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CarritoControlador {

    private final CarritoServicio carritoServicio;
    private final UsuarioRepositorio usuarioRepositorio;

    private Long obtenerUsuarioId(Authentication auth) {
        String username = auth.getName();
        Usuario usuario = usuarioRepositorio.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        return usuario.getId();
    }

    // Obtener carrito actual (ABIERTO) del usuario
    @GetMapping("/mio")
    public Carrito obtenerMiCarrito(Authentication auth) {
        Long usuarioId = obtenerUsuarioId(auth);
        return carritoServicio.obtenerCarritoAbiertoPorUsuario(usuarioId);
    }

    // Agregar producto al carrito
    @PostMapping("/agregar")
    public Carrito agregarProducto(
            @RequestParam("productoId") Long productoId,
            @RequestParam("cantidad") int cantidad,
            Authentication auth
    ) {
        Long usuarioId = obtenerUsuarioId(auth);
        return carritoServicio.agregarProducto(usuarioId, productoId, cantidad);
    }

    // Cambiar cantidad de un producto en el carrito
    @PutMapping("/cantidad")
    public Carrito actualizarCantidad(
            @RequestParam("productoId") Long productoId,
            @RequestParam("cantidad") int cantidad,
            Authentication auth
    ) {
        Long usuarioId = obtenerUsuarioId(auth);
        return carritoServicio.actualizarCantidad(usuarioId, productoId, cantidad);
    }

    // Vaciar carrito
    @PostMapping("/vaciar")
    public Carrito vaciarCarrito(Authentication auth) {
        Long usuarioId = obtenerUsuarioId(auth);
        return carritoServicio.vaciarCarrito(usuarioId);
    }

    // Finalizar compra (crear pedido y abrir nuevo carrito ABIERTO)
    @PostMapping("/comprar")
    public Pedido comprar(Authentication auth) {
        Long usuarioId = obtenerUsuarioId(auth);
        return carritoServicio.comprar(usuarioId);
    }
}

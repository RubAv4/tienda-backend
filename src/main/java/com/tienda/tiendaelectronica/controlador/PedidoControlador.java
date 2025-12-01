/* src/main/java/com/tienda/tiendaelectronica/controlador/PedidoControlador.java */
package com.tienda.tiendaelectronica.controlador;

import com.tienda.tiendaelectronica.excepciones.RecursoNoEncontradoException;
import com.tienda.tiendaelectronica.modelo.Pedido;
import com.tienda.tiendaelectronica.modelo.Usuario;
import com.tienda.tiendaelectronica.repositorio.UsuarioRepositorio;
import com.tienda.tiendaelectronica.servicio.PedidoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class PedidoControlador {

    private final PedidoServicio pedidoServicio;
    private final UsuarioRepositorio usuarioRepositorio;

    /**
     * Listar pedidos del usuario logueado.
     */
    @GetMapping("/mis")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Pedido> listarMisPedidos(Authentication auth) {

        String username = auth.getName();

        Usuario usuario = usuarioRepositorio.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        return pedidoServicio.listarPorUsuario(usuario.getId());
    }
}

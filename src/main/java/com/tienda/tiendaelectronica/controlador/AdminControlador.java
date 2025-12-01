/* src/main/java/com/tienda/tiendaelectronica/controlador/AdminControlador.java */
package com.tienda.tiendaelectronica.controlador;

import com.tienda.tiendaelectronica.excepciones.RecursoNoEncontradoException;
import com.tienda.tiendaelectronica.modelo.Pedido;
import com.tienda.tiendaelectronica.modelo.Producto;
import com.tienda.tiendaelectronica.modelo.LogReponedor;
import com.tienda.tiendaelectronica.modelo.Categoria;
import com.tienda.tiendaelectronica.repositorio.CategoriaRepositorio;
import com.tienda.tiendaelectronica.repositorio.ProductoRepositorio;
import com.tienda.tiendaelectronica.servicio.PedidoServicio;
import com.tienda.tiendaelectronica.servicio.ProductoServicio;
import com.tienda.tiendaelectronica.servicio.LogReponedorServicio;
import com.tienda.tiendaelectronica.servicio.CategoriaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminControlador {

    private final PedidoServicio pedidoServicio;
    private final ProductoServicio productoServicio;
    private final LogReponedorServicio logReponedorServicio;

    private final CategoriaServicio categoriaServicio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final ProductoRepositorio productoRepositorio;

    // =====================================================================================
    // ===================================== DTOs =========================================
    // =====================================================================================

    public static class CrearProductoRequest {
        public Long categoriaId;
        public String nombre;
        public String descripcion;
        public BigDecimal precio;
        public Integer stock;
        public String imagenUrl;
    }

    public static class ActualizarProductoRequest {
        public Long categoriaId;
        public String nombre;
        public String descripcion;
        public BigDecimal precio;
        public Integer stock;
        public String imagenUrl;
    }

    // =====================================================================================
    // ======================= DASHBOARD (ADMIN + REPONEDOR) ===============================
    // =====================================================================================

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','REPONEDOR')")
    public Map<String, Object> obtenerDashboard() {
        Map<String, Object> data = new HashMap<>();

        try { data.put("pedidos", pedidoServicio.listarTodos()); }
        catch (Exception e) { data.put("pedidos", Collections.emptyList()); }

        try { data.put("productos", productoServicio.listarTodos()); }
        catch (Exception e) { data.put("productos", Collections.emptyList()); }

        try { data.put("logsReponedor", logReponedorServicio.listarTodos()); }
        catch (Exception e) { data.put("logsReponedor", Collections.emptyList()); }

        try { data.put("ventasPorUsuario", pedidoServicio.resumenVentasPorUsuario()); }
        catch (Exception e) { data.put("ventasPorUsuario", Collections.emptyList()); }

        return data;
    }

    // =====================================================================================
    // =================================== CATEGORÍAS ======================================
    // =====================================================================================

    @GetMapping("/categorias")
    @PreAuthorize("hasAnyRole('ADMIN','REPONEDOR')")
    public List<Categoria> listarCategoriasAdmin() {
        return categoriaRepositorio.findAll();
    }

    @PostMapping("/categorias")
    @PreAuthorize("hasRole('ADMIN')")
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        try {
            categoria.setId(null);
            return categoriaServicio.crear(categoria);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear categoría", e);
        }
    }

    // ===== ACTUALIZAR CATEGORÍA =====
    @PutMapping("/categorias/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Categoria actualizarCategoria(@PathVariable Long id, @RequestBody Categoria datos) {
        Categoria cat = categoriaRepositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        cat.setNombre(datos.getNombre());
        cat.setDescripcion(datos.getDescripcion());

        // si tu entidad tiene campo activo
        try {
            cat.setActivo(datos.getActivo());
        } catch (Exception ignored) { }

        return categoriaRepositorio.save(cat);
    }

    // =====================================================================================
    // ===================================== PRODUCTOS =====================================
    // =====================================================================================

    @PostMapping("/productos")
    @PreAuthorize("hasRole('ADMIN')")
    public Producto crearProducto(@RequestBody CrearProductoRequest req,
                                  Authentication auth) {

        if (req.categoriaId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "categoriaId es obligatorio");
        }

        try {
            Categoria categoria = categoriaServicio.obtenerPorId(req.categoriaId);

            Producto producto = new Producto();
            producto.setNombre(req.nombre);
            producto.setDescripcion(req.descripcion);
            producto.setPrecio(req.precio);
            producto.setStock(req.stock != null ? req.stock : 0);
            producto.setImagenUrl(req.imagenUrl);
            producto.setActivo(true);
            producto.setCategoria(categoria);

            Producto guardado = productoServicio.crear(producto);

            String username = (auth != null) ? auth.getName() : "system";
            logReponedorServicio.registrarAccion(username, guardado.getId(), 0, "Creó producto desde panel admin");

            return guardado;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear producto", e);
        }
    }

    // ===== ACTUALIZAR PRODUCTO =====
    @PutMapping("/productos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Producto actualizarProducto(
            @PathVariable Long id,
            @RequestBody ActualizarProductoRequest req
    ) {
        Producto p = productoRepositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (req.categoriaId != null && req.categoriaId > 0) {
            Categoria categoria = categoriaRepositorio.findById(req.categoriaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no válida"));
            p.setCategoria(categoria);
        }

        p.setNombre(req.nombre);
        p.setDescripcion(req.descripcion);
        p.setPrecio(req.precio);
        p.setStock(req.stock);
        p.setImagenUrl(req.imagenUrl);

        return productoRepositorio.save(p);
    }

    // =====================================================================================
    // =========================== STOCK / ACTIVO / ELIMINAR ===============================
    // =====================================================================================

    @PutMapping("/productos/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN','REPONEDOR')")
    public Producto actualizarStock(@PathVariable Long id,
                                    @RequestParam("stock") Integer stock,
                                    Authentication auth) {

        Producto antes = productoServicio.obtenerPorId(id);
        Integer stockAnterior = antes.getStock();

        Producto despues = productoServicio.actualizarStock(id, stock);

        logReponedorServicio.registrarAccion(
                auth.getName(),
                id,
                stock - stockAnterior,
                "Actualizó stock"
        );

        return despues;
    }

    @PutMapping("/productos/{id}/activo")
    @PreAuthorize("hasAnyRole('ADMIN','REPONEDOR')")
    public Producto cambiarEstadoActivo(@PathVariable Long id,
                                        @RequestParam("activo") boolean activo,
                                        Authentication auth) {

        Producto producto = productoServicio.cambiarEstadoActivo(id, activo);

        logReponedorServicio.registrarAccion(
                auth.getName(),
                id,
                0,
                activo ? "Producto activado" : "Producto desactivado"
        );

        return producto;
    }

    @DeleteMapping("/productos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminarProducto(@PathVariable Long id,
                                 Authentication auth) {
        productoServicio.eliminarLogico(id);

        logReponedorServicio.registrarAccion(
                auth.getName(),
                id,
                0,
                "Producto eliminado"
        );
    }
}

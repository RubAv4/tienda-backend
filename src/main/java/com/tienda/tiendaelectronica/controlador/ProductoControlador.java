package com.tienda.tiendaelectronica.controlador;

import com.tienda.tiendaelectronica.modelo.Producto;
import com.tienda.tiendaelectronica.servicio.ProductoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ProductoControlador {

    private final ProductoServicio productoServicio;

    // ----------- PÚBLICO / TIENDA ------------

    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> listarActivos() {
        return ResponseEntity.ok(productoServicio.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoServicio.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoServicio.buscarPorNombre(nombre));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoServicio.listarPorCategoria(categoriaId));
    }

    // ----------- ADMIN / REPONEDOR ------------

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPONEDOR')")
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoServicio.listarTodos());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPONEDOR')")
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoServicio.crear(producto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPONEDOR')")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id,
            @RequestBody Producto producto) {
        return ResponseEntity.ok(productoServicio.actualizar(id, producto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // eliminación lógica: marca activo = false
        productoServicio.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'REPONEDOR')")
    public ResponseEntity<Producto> actualizarStock(@PathVariable Long id,
            @RequestBody Map<String, Integer> body) {
        Integer stock = body.get("stock");
        if (stock == null) {
            return ResponseEntity.badRequest().build();
        }
        Producto actualizado = productoServicio.actualizarStock(id, stock);
        return ResponseEntity.ok(actualizado);
    }

}

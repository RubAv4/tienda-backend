/* src/main/java/com/tienda/tiendaelectronica/controlador/CategoriaControlador.java */
package com.tienda.tiendaelectronica.controlador;

import com.tienda.tiendaelectronica.modelo.Categoria;
import com.tienda.tiendaelectronica.servicio.CategoriaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CategoriaControlador {

    private final CategoriaServicio categoriaServicio;

    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas() {
        return ResponseEntity.ok(categoriaServicio.listarTodas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Categoria>> listarActivas() {
        return ResponseEntity.ok(categoriaServicio.listarActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaServicio.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaServicio.crear(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaServicio.actualizar(id, categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        categoriaServicio.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}

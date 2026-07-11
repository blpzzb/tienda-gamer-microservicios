package cl.duoc.tienda.inventario.controller;

import cl.duoc.tienda.inventario.dto.InventarioRequest;
import cl.duoc.tienda.inventario.model.Inventario;
import cl.duoc.tienda.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
@Tag(name = "Inventario", description = "CRUD de inventario y control de stock")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    @Operation(summary = "Listar todo el inventario")
    public ResponseEntity<List<Inventario>> getAll() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar inventario por ID")
    public ResponseEntity<Inventario> getById(@PathVariable Long id) {
        Inventario inventario = inventarioService.buscarPorId(id);
        return ResponseEntity.ok(inventario);
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Buscar inventario por ID de producto")
    public ResponseEntity<Inventario> getByProductoId(@PathVariable Long productoId) {
        Inventario inventario = inventarioService.buscarPorProductoId(productoId);
        return ResponseEntity.ok(inventario);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo registro de inventario")
    public ResponseEntity<Inventario> create(@Valid @RequestBody InventarioRequest request) {
        Inventario nuevo = inventarioService.guardar(request);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar registro de inventario por ID")
    public ResponseEntity<Inventario> update(@PathVariable Long id, @Valid @RequestBody InventarioRequest request) {
        return ResponseEntity.ok(inventarioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro de inventario por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

package cl.duoc.tienda.inventario.controller;

import cl.duoc.tienda.inventario.model.Inventario;
import cl.duoc.tienda.inventario.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<Inventario>> getAll() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> getById(@PathVariable Long id) {
        Inventario inventario = inventarioService.buscarPorId(id);
        return ResponseEntity.ok(inventario);
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Inventario> getByProductoId(@PathVariable Long productoId) {
        Inventario inventario = inventarioService.buscarPorProductoId(productoId);
        return ResponseEntity.ok(inventario);
    }

    @PostMapping
    public ResponseEntity<Inventario> create(@RequestBody Inventario inventario) {
        Inventario nuevo = inventarioService.guardar(inventario);
        return ResponseEntity.status(201).body(nuevo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
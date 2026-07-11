package cl.duoc.tienda.orden.controller;

import cl.duoc.tienda.orden.dto.OrdenRequest;
import cl.duoc.tienda.orden.model.Orden;
import cl.duoc.tienda.orden.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
@Tag(name = "Orden", description = "CRUD y orquestación de órdenes de compra")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @GetMapping
    @Operation(summary = "Listar todas las órdenes")
    public ResponseEntity<List<Orden>> getAll() {
        return ResponseEntity.ok(ordenService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar orden por ID")
    public ResponseEntity<Orden> getById(@PathVariable Long id) {
        Orden orden = ordenService.buscarPorId(id);
        return ResponseEntity.ok(orden);
    }

    @PostMapping
    @Operation(summary = "Crear nueva orden (flujo distribuido)")
    public ResponseEntity<Orden> create(@Valid @RequestBody OrdenRequest request) {
        Orden nuevaOrden = ordenService.guardar(request);
        return ResponseEntity.status(201).body(nuevaOrden);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar orden por ID")
    public ResponseEntity<Orden> update(@PathVariable Long id, @Valid @RequestBody OrdenRequest request) {
        return ResponseEntity.ok(ordenService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar orden por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ordenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

package cl.duoc.tienda.orden.controller;

import cl.duoc.tienda.orden.dto.OrdenRequest;
import cl.duoc.tienda.orden.model.Orden;
import cl.duoc.tienda.orden.service.OrdenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @GetMapping
    public ResponseEntity<List<Orden>> getAll() {
        return ResponseEntity.ok(ordenService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> getById(@PathVariable Long id) {
        Orden orden = ordenService.buscarPorId(id);
        return ResponseEntity.ok(orden);
    }

    @PostMapping
    public ResponseEntity<Orden> create(@Valid @RequestBody OrdenRequest request) {
        Orden nuevaOrden = ordenService.guardar(request);
        return ResponseEntity.status(201).body(nuevaOrden);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orden> update(@PathVariable Long id, @Valid @RequestBody OrdenRequest request) {
        return ResponseEntity.ok(ordenService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ordenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

package cl.duoc.tienda.pago.controller;

import cl.duoc.tienda.pago.dto.PagoRequest;
import cl.duoc.tienda.pago.model.Pago;
import cl.duoc.tienda.pago.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
@Tag(name = "Pago", description = "CRUD de pagos y registro de transacciones")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    @Operation(summary = "Listar todos los pagos")
    public ResponseEntity<List<Pago>> getAll() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pago por ID")
    public ResponseEntity<Pago> getById(@PathVariable Long id) {
        Pago pago = pagoService.buscarPorId(id);
        return ResponseEntity.ok(pago);
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo pago (valida orden)")
    public ResponseEntity<Pago> create(@Valid @RequestBody PagoRequest request) {
        Pago nuevo = pagoService.guardar(request);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pago por ID")
    public ResponseEntity<Pago> update(@PathVariable Long id, @Valid @RequestBody PagoRequest request) {
        return ResponseEntity.ok(pagoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pago por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

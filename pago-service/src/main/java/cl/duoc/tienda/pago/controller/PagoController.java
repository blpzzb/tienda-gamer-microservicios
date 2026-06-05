package cl.duoc.tienda.pago.controller;

import cl.duoc.tienda.pago.model.Pago;
import cl.duoc.tienda.pago.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> getAll() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> getById(@PathVariable Long id) {
        Pago pago = pagoService.buscarPorId(id);
        return ResponseEntity.ok(pago);
    }

    @PostMapping
    public ResponseEntity<Pago> create(@RequestBody Pago pago) {
        Pago nuevo = pagoService.guardar(pago);
        return ResponseEntity.status(201).body(nuevo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
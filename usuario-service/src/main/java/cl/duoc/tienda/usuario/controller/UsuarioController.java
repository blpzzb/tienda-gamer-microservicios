package cl.duoc.tienda.usuario.controller;

import cl.duoc.tienda.usuario.dto.UsuarioRequest;
import cl.duoc.tienda.usuario.model.Usuario;
import cl.duoc.tienda.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuario", description = "CRUD de usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<Usuario>> getAll() {
        logger.info("Petición GET /usuarios recibida");
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        logger.info("Petición GET /usuarios/{} recibida", id);
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario")
    public ResponseEntity<Usuario> create(@Valid @RequestBody UsuarioRequest request) {
        logger.info("Petición POST /usuarios recibida para crear usuario");
        Usuario nuevo = usuarioService.guardar(request);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario por ID")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        logger.info("Petición PUT /usuarios/{} recibida para actualizar usuario", id);
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Petición DELETE /usuarios/{} recibida para eliminar usuario", id);
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

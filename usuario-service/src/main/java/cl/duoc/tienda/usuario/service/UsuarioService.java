package cl.duoc.tienda.usuario.service;

import cl.duoc.tienda.usuario.dto.UsuarioRequest;
import cl.duoc.tienda.usuario.model.Usuario;
import cl.duoc.tienda.usuario.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        logger.info("Listando todos los usuarios");
        return usuarioRepository.findAll();
    }

    public Usuario guardar(UsuarioRequest request) {
        logger.info("Intentando guardar usuario: {}", request.correo());
        
        if (request.nombre() == null || request.nombre().trim().isEmpty()) {
            logger.warn("No se pudo guardar el usuario: nombre vacío");
            throw new IllegalArgumentException("El nombre del usuario es obligatorio");
        }
        
        if (request.correo() == null || request.correo().trim().isEmpty()) {
            logger.warn("No se pudo guardar el usuario: correo vacío");
            throw new IllegalArgumentException("El correo del usuario es obligatorio");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setCorreo(request.correo());
        usuario.setRut(request.rut());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        logger.info("Usuario guardado correctamente con ID: {}", usuarioGuardado.getId());
        return usuarioGuardado;
    }

    public Usuario actualizar(Long id, UsuarioRequest request) {
        logger.info("Intentando actualizar usuario con ID: {}", id);
        buscarPorId(id);
        
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre(request.nombre());
        usuario.setCorreo(request.correo());
        usuario.setRut(request.rut());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        logger.info("Usuario guardado correctamente con ID: {}", usuarioGuardado.getId());
        return usuarioGuardado;
    }

    public void eliminar(Long id) {
        logger.info("Intentando eliminar usuario con ID: {}", id);
        
        if (id == null) {
            logger.warn("No se pudo eliminar usuario: ID nulo");
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }

        if (!usuarioRepository.existsById(id)) {
            logger.warn("No se encontró usuario con ID: {}", id);
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }

        usuarioRepository.deleteById(id);
        logger.info("Usuario eliminado correctamente con ID: {}", id);
    }

    public Usuario buscarPorId(Long id) {
        logger.info("Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Usuario no encontrado con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado con ID: " + id);
                });
    }
}

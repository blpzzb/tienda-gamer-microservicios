package cl.duoc.tienda.inventario.service;

import cl.duoc.tienda.inventario.model.Inventario;
import cl.duoc.tienda.inventario.repository.InventarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> listarTodos() {
        logger.info("Listando todo el inventario");
        return inventarioRepository.findAll();
    }

    public Inventario buscarPorId(Long id) {
        logger.info("Buscando inventario con ID: {}", id);

        return inventarioRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Inventario no encontrado con ID: {}", id);
                    return new RuntimeException("Inventario no encontrado con ID: " + id);
                });
    }

    public Inventario buscarPorProductoId(Long productoId) {
        logger.info("Buscando inventario para producto ID: {}", productoId);

        if (productoId == null) {
            logger.warn("No se pudo buscar inventario: productoId nulo");
            throw new IllegalArgumentException("El productoId es obligatorio");
        }

        return inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> {
                    logger.warn("Inventario no encontrado para producto ID: {}", productoId);
                    return new RuntimeException("Inventario no encontrado para producto ID: " + productoId);
                });
    }

    public Inventario guardar(Inventario inventario) {
        logger.info("Intentando guardar inventario para producto ID: {}", inventario.getProductoId());

        if (inventario.getProductoId() == null) {
            logger.warn("No se pudo guardar inventario: productoId vacío");
            throw new IllegalArgumentException("El productoId es obligatorio");
        }

        if (inventario.getStockActual() == null || inventario.getStockActual() < 0) {
            logger.warn("No se pudo guardar inventario: stock inválido");
            throw new IllegalArgumentException("El stock actual no puede ser negativo");
        }

        if (inventario.getUbicacionBodega() == null || inventario.getUbicacionBodega().trim().isEmpty()) {
            logger.warn("No se pudo guardar inventario: ubicación de bodega vacía");
            throw new IllegalArgumentException("La ubicación de bodega es obligatoria");
        }

        Inventario inventarioGuardado = inventarioRepository.save(inventario);

        logger.info("Inventario guardado correctamente con ID: {}", inventarioGuardado.getId());

        return inventarioGuardado;
    }

    public void eliminar(Long id) {
        logger.info("Intentando eliminar inventario con ID: {}", id);

        if (id == null) {
            logger.warn("No se pudo eliminar inventario: ID nulo");
            throw new IllegalArgumentException("El ID del inventario es obligatorio");
        }

        if (!inventarioRepository.existsById(id)) {
            logger.warn("No se encontró inventario con ID: {}", id);
            throw new RuntimeException("Inventario no encontrado con ID: " + id);
        }

        inventarioRepository.deleteById(id);

        logger.info("Inventario eliminado correctamente con ID: {}", id);
    }
}
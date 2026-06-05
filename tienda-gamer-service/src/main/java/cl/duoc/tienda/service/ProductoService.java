package cl.duoc.tienda.service;

import cl.duoc.tienda.model.Producto;
import cl.duoc.tienda.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        logger.info("Listando todos los productos");
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Long id) {
        logger.info("Buscando producto con ID: {}", id);

        return productoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Producto no encontrado con ID: {}", id);
                    return new RuntimeException("Producto no encontrado con ID: " + id);
                });
    }

    public Producto guardar(Producto producto) {
        logger.info("Intentando guardar producto: {}", producto.getNombre());

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            logger.warn("No se pudo guardar el producto: nombre vacío");
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            logger.warn("No se pudo guardar el producto: precio inválido");
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        if (producto.getStock() == null || producto.getStock() < 0) {
            logger.warn("No se pudo guardar el producto: stock inválido");
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        Producto productoGuardado = productoRepository.save(producto);

        logger.info("Producto guardado correctamente con ID: {}", productoGuardado.getId());

        return productoGuardado;
    }

    public void eliminar(Long id) {
        logger.info("Intentando eliminar producto con ID: {}", id);

        if (id == null) {
            logger.warn("No se pudo eliminar producto: ID nulo");
            throw new IllegalArgumentException("El ID del producto es obligatorio");
        }

        if (!productoRepository.existsById(id)) {
            logger.warn("No se encontró producto con ID: {}", id);
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }

        productoRepository.deleteById(id);
        logger.info("Producto eliminado correctamente con ID: {}", id);
    }
}
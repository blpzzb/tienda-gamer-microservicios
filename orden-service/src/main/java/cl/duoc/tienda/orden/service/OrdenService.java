package cl.duoc.tienda.orden.service;

import cl.duoc.tienda.orden.dto.InventarioDTO;
import cl.duoc.tienda.orden.dto.ProductoDTO;
import cl.duoc.tienda.orden.dto.UsuarioDTO;
import cl.duoc.tienda.orden.model.Orden;
import cl.duoc.tienda.orden.repository.OrdenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdenService {

    private static final Logger logger = LoggerFactory.getLogger(OrdenService.class);

    private final OrdenRepository ordenRepository;
    private final WebClient webClient;

    @Value("${services.usuario.url}")
    private String usuarioServiceUrl;

    @Value("${services.producto.url}")
    private String productoServiceUrl;

    @Value("${services.inventario.url}")
    private String inventarioServiceUrl;

    public OrdenService(OrdenRepository ordenRepository, WebClient webClient) {
        this.ordenRepository = ordenRepository;
        this.webClient = webClient;
    }

    public List<Orden> listarTodas() {
        logger.info("Listando todas las órdenes");
        return ordenRepository.findAll();
    }

    public Orden buscarPorId(Long id) {
        logger.info("Buscando orden con ID: {}", id);

        if (id == null) {
            logger.warn("No se pudo buscar la orden: ID nulo");
            throw new IllegalArgumentException("El ID de la orden es obligatorio");
        }

        return ordenRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Orden no encontrada con ID: {}", id);
                    return new RuntimeException("Orden no encontrada con ID: " + id);
                });
    }

    public Orden guardar(Orden orden) {
        logger.info("Iniciando creación de orden");

        validarOrden(orden);

        UsuarioDTO usuario = consultarUsuario(orden.getUsuarioId());
        ProductoDTO producto = consultarProducto(orden.getProductoId());
        InventarioDTO inventario = consultarInventario(orden.getProductoId());

        logger.info("Usuario validado desde usuario-service: {}", usuario.getNombre());
        logger.info("Producto validado desde tienda-gamer-service: {}", producto.getNombre());
        logger.info("Stock consultado desde inventario-service: {}", inventario.getStockActual());

        if (inventario.getStockActual() == null || inventario.getStockActual() < orden.getCantidad()) {
            logger.warn("Stock insuficiente para producto ID: {}", orden.getProductoId());
            throw new RuntimeException("Stock insuficiente para crear la orden");
        }

        orden.setFechaOrden(LocalDateTime.now());

        Orden ordenGuardada = ordenRepository.save(orden);

        logger.info("Orden creada correctamente con ID: {}", ordenGuardada.getId());

        return ordenGuardada;
    }

    public void eliminar(Long id) {
        logger.info("Intentando eliminar orden con ID: {}", id);

        if (id == null) {
            logger.warn("No se pudo eliminar la orden: ID nulo");
            throw new IllegalArgumentException("El ID de la orden es obligatorio");
        }

        if (!ordenRepository.existsById(id)) {
            logger.warn("No se encontró orden con ID: {}", id);
            throw new RuntimeException("Orden no encontrada con ID: " + id);
        }

        ordenRepository.deleteById(id);

        logger.info("Orden eliminada correctamente con ID: {}", id);
    }

    private void validarOrden(Orden orden) {
        if (orden.getUsuarioId() == null) {
            logger.warn("No se pudo crear la orden: usuarioId vacío");
            throw new IllegalArgumentException("El usuarioId es obligatorio");
        }

        if (orden.getProductoId() == null) {
            logger.warn("No se pudo crear la orden: productoId vacío");
            throw new IllegalArgumentException("El productoId es obligatorio");
        }

        if (orden.getCantidad() == null || orden.getCantidad() <= 0) {
            logger.warn("No se pudo crear la orden: cantidad inválida");
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
    }

    private UsuarioDTO consultarUsuario(Long usuarioId) {
        logger.info("Consultando usuario-service con ID: {}", usuarioId);

        try {
            return webClient.get()
                    .uri(usuarioServiceUrl + "/usuarios/{id}", usuarioId)
                    .retrieve()
                    .bodyToMono(UsuarioDTO.class)
                    .block(Duration.ofSeconds(5));
        } catch (WebClientResponseException e) {
            logger.error("Error al consultar usuario-service. Código HTTP: {}", e.getStatusCode());
            throw new RuntimeException("No se pudo validar el usuario con ID: " + usuarioId);
        } catch (Exception e) {
            logger.error("usuario-service no responde: {}", e.getMessage());
            throw new RuntimeException("usuario-service no responde");
        }
    }

    private ProductoDTO consultarProducto(Long productoId) {
        logger.info("Consultando tienda-gamer-service con ID: {}", productoId);

        try {
            return webClient.get()
                    .uri(productoServiceUrl + "/productos/{id}", productoId)
                    .retrieve()
                    .bodyToMono(ProductoDTO.class)
                    .block(Duration.ofSeconds(5));
        } catch (WebClientResponseException e) {
            logger.error("Error al consultar tienda-gamer-service. Código HTTP: {}", e.getStatusCode());
            throw new RuntimeException("No se pudo validar el producto con ID: " + productoId);
        } catch (Exception e) {
            logger.error("tienda-gamer-service no responde: {}", e.getMessage());
            throw new RuntimeException("tienda-gamer-service no responde");
        }
    }

    private InventarioDTO consultarInventario(Long productoId) {
        logger.info("Consultando inventario-service para producto ID: {}", productoId);

        try {
            return webClient.get()
                    .uri(inventarioServiceUrl + "/inventario/producto/{productoId}", productoId)
                    .retrieve()
                    .bodyToMono(InventarioDTO.class)
                    .block(Duration.ofSeconds(5));
        } catch (WebClientResponseException e) {
            logger.error("Error al consultar inventario-service. Código HTTP: {}", e.getStatusCode());
            throw new RuntimeException("No se pudo validar inventario para producto ID: " + productoId);
        } catch (Exception e) {
            logger.error("inventario-service no responde: {}", e.getMessage());
            throw new RuntimeException("inventario-service no responde");
        }
    }
}
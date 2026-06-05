package cl.duoc.tienda.pago.service;

import cl.duoc.tienda.pago.dto.OrdenDTO;
import cl.duoc.tienda.pago.model.Pago;
import cl.duoc.tienda.pago.repository.PagoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;

@Service
public class PagoService {

    private static final Logger logger = LoggerFactory.getLogger(PagoService.class);

    private final PagoRepository pagoRepository;
    private final WebClient webClient;

    @Value("${services.orden.url}")
    private String ordenServiceUrl;

    public PagoService(PagoRepository pagoRepository, WebClient webClient) {
        this.pagoRepository = pagoRepository;
        this.webClient = webClient;
    }

    public List<Pago> listarTodos() {
        logger.info("Listando todos los pagos");
        return pagoRepository.findAll();
    }

    public Pago buscarPorId(Long id) {
        logger.info("Buscando pago con ID: {}", id);

        if (id == null) {
            logger.warn("No se pudo buscar el pago: ID nulo");
            throw new IllegalArgumentException("El ID del pago es obligatorio");
        }

        return pagoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Pago no encontrado con ID: {}", id);
                    return new RuntimeException("Pago no encontrado con ID: " + id);
                });
    }

    public Pago guardar(Pago pago) {
        logger.info("Iniciando registro de pago para orden ID: {}", pago.getOrdenId());

        validarPago(pago);

        OrdenDTO orden = consultarOrden(pago.getOrdenId());

        logger.info("Orden validada desde orden-service. ID: {}", orden.getId());

        if (pago.getEstado() == null || pago.getEstado().trim().isEmpty()) {
            pago.setEstado("PAGADO");
            logger.info("Estado del pago asignado automáticamente como PAGADO");
        }

        Pago pagoGuardado = pagoRepository.save(pago);

        logger.info("Pago registrado correctamente con ID: {}", pagoGuardado.getId());

        return pagoGuardado;
    }

    public void eliminar(Long id) {
        logger.info("Intentando eliminar pago con ID: {}", id);

        if (id == null) {
            logger.warn("No se pudo eliminar el pago: ID nulo");
            throw new IllegalArgumentException("El ID del pago es obligatorio");
        }

        if (!pagoRepository.existsById(id)) {
            logger.warn("No se encontró pago con ID: {}", id);
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }

        pagoRepository.deleteById(id);

        logger.info("Pago eliminado correctamente con ID: {}", id);
    }

    private void validarPago(Pago pago) {
        if (pago.getOrdenId() == null) {
            logger.warn("No se pudo registrar el pago: ordenId vacío");
            throw new IllegalArgumentException("El ordenId es obligatorio");
        }

        if (pago.getMonto() == null || pago.getMonto() <= 0) {
            logger.warn("No se pudo registrar el pago: monto inválido");
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
    }

    private OrdenDTO consultarOrden(Long ordenId) {
        logger.info("Consultando orden-service con ID: {}", ordenId);

        try {
            return webClient.get()
                    .uri(ordenServiceUrl + "/ordenes/{id}", ordenId)
                    .retrieve()
                    .bodyToMono(OrdenDTO.class)
                    .block(Duration.ofSeconds(5));
        } catch (WebClientResponseException e) {
            logger.error("Error al consultar orden-service. Código HTTP: {}", e.getStatusCode());
            throw new RuntimeException("No se pudo validar la orden con ID: " + ordenId);
        } catch (Exception e) {
            logger.error("orden-service no responde: {}", e.getMessage());
            throw new RuntimeException("orden-service no responde");
        }
    }
}
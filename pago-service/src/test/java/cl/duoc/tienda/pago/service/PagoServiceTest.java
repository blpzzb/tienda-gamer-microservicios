package cl.duoc.tienda.pago.service;

import cl.duoc.tienda.pago.dto.OrdenDTO;
import cl.duoc.tienda.pago.model.Pago;
import cl.duoc.tienda.pago.repository.PagoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private PagoService pagoService;

    @Test
    void listarTodos_deberiaRetornarListaDePagos() {
        // Given
        Pago pago1 = new Pago();
        pago1.setId(1L);
        pago1.setOrdenId(1L);
        pago1.setMonto(50000.0);
        pago1.setEstado("PAGADO");

        Pago pago2 = new Pago();
        pago2.setId(2L);
        pago2.setOrdenId(2L);
        pago2.setMonto(30000.0);
        pago2.setEstado("PAGADO");

        when(pagoRepository.findAll()).thenReturn(Arrays.asList(pago1, pago2));

        // When
        List<Pago> resultado = pagoService.listarTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getOrdenId());
        assertEquals(2L, resultado.get(1).getOrdenId());

        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarPago() {
        // Given
        Long id = 1L;

        Pago pago = new Pago();
        pago.setId(id);
        pago.setOrdenId(1L);
        pago.setMonto(50000.0);
        pago.setEstado("PAGADO");

        when(pagoRepository.findById(id)).thenReturn(Optional.of(pago));

        // When
        Pago resultado = pagoService.buscarPorId(id);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(1L, resultado.getOrdenId());
        assertEquals(50000.0, resultado.getMonto());
        assertEquals("PAGADO", resultado.getEstado());

        verify(pagoRepository, times(1)).findById(id);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        Long id = 99L;

        when(pagoRepository.findById(id)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.buscarPorId(id);
        });

        // Then
        assertEquals("Pago no encontrado con ID: 99", exception.getMessage());

        verify(pagoRepository, times(1)).findById(id);
    }

    @Test
    void guardar_conPagoValido_deberiaGuardarPagoCorrectamente() {
        // Given
        ReflectionTestUtils.setField(pagoService, "ordenServiceUrl", "http://localhost:8082");

        Pago pago = new Pago();
        pago.setOrdenId(1L);
        pago.setMonto(50000.0);
        pago.setEstado("PAGADO");

        OrdenDTO ordenDTO = new OrdenDTO();
        ordenDTO.setId(1L);
        ordenDTO.setUsuarioId(1L);
        ordenDTO.setProductoId(1L);
        ordenDTO.setCantidad(2);
        ordenDTO.setFechaOrden(LocalDateTime.now());

        Pago pagoGuardado = new Pago();
        pagoGuardado.setId(1L);
        pagoGuardado.setOrdenId(1L);
        pagoGuardado.setMonto(50000.0);
        pagoGuardado.setEstado("PAGADO");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OrdenDTO.class)).thenReturn(Mono.just(ordenDTO));
        when(pagoRepository.save(pago)).thenReturn(pagoGuardado);

        // When
        Pago resultado = pagoService.guardar(pago);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getOrdenId());
        assertEquals(50000.0, resultado.getMonto());
        assertEquals("PAGADO", resultado.getEstado());

        verify(pagoRepository, times(1)).save(pago);
    }

    @Test
    void guardar_conEstadoVacio_deberiaAsignarEstadoPagado() {
        // Given
        ReflectionTestUtils.setField(pagoService, "ordenServiceUrl", "http://localhost:8082");

        Pago pago = new Pago();
        pago.setOrdenId(1L);
        pago.setMonto(50000.0);
        pago.setEstado("");

        OrdenDTO ordenDTO = new OrdenDTO();
        ordenDTO.setId(1L);
        ordenDTO.setUsuarioId(1L);
        ordenDTO.setProductoId(1L);
        ordenDTO.setCantidad(2);

        Pago pagoGuardado = new Pago();
        pagoGuardado.setId(1L);
        pagoGuardado.setOrdenId(1L);
        pagoGuardado.setMonto(50000.0);
        pagoGuardado.setEstado("PAGADO");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OrdenDTO.class)).thenReturn(Mono.just(ordenDTO));
        when(pagoRepository.save(pago)).thenReturn(pagoGuardado);

        // When
        Pago resultado = pagoService.guardar(pago);

        // Then
        assertNotNull(resultado);
        assertEquals("PAGADO", pago.getEstado());
        assertEquals("PAGADO", resultado.getEstado());

        verify(pagoRepository, times(1)).save(pago);
    }

    @Test
    void guardar_conOrdenIdNulo_deberiaLanzarExcepcion() {
        // Given
        Pago pago = new Pago();
        pago.setOrdenId(null);
        pago.setMonto(50000.0);
        pago.setEstado("PAGADO");

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pagoService.guardar(pago);
        });

        // Then
        assertEquals("El ordenId es obligatorio", exception.getMessage());

        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void guardar_conMontoInvalido_deberiaLanzarExcepcion() {
        // Given
        Pago pago = new Pago();
        pago.setOrdenId(1L);
        pago.setMonto(0.0);
        pago.setEstado("PAGADO");

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pagoService.guardar(pago);
        });

        // Then
        assertEquals("El monto debe ser mayor a 0", exception.getMessage());

        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void eliminar_cuandoExiste_deberiaEliminarPago() {
        // Given
        Long id = 1L;

        when(pagoRepository.existsById(id)).thenReturn(true);
        doNothing().when(pagoRepository).deleteById(id);

        // When
        pagoService.eliminar(id);

        // Then
        verify(pagoRepository, times(1)).existsById(id);
        verify(pagoRepository, times(1)).deleteById(id);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        Long id = 99L;

        when(pagoRepository.existsById(id)).thenReturn(false);

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.eliminar(id);
        });

        // Then
        assertEquals("Pago no encontrado con ID: 99", exception.getMessage());

        verify(pagoRepository, times(1)).existsById(id);
        verify(pagoRepository, never()).deleteById(id);
    }

    @Test
    void eliminar_conIdNulo_deberiaLanzarExcepcion() {
        // Given
        Long id = null;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pagoService.eliminar(id);
        });

        // Then
        assertEquals("El ID del pago es obligatorio", exception.getMessage());

        verify(pagoRepository, never()).existsById(any());
        verify(pagoRepository, never()).deleteById(any());
    }
}
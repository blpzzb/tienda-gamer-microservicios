package cl.duoc.tienda.orden.service;

import cl.duoc.tienda.orden.dto.InventarioDTO;
import cl.duoc.tienda.orden.dto.ProductoDTO;
import cl.duoc.tienda.orden.dto.UsuarioDTO;
import cl.duoc.tienda.orden.model.Orden;
import cl.duoc.tienda.orden.repository.OrdenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private OrdenService ordenService;

    @Test
    void listarTodas_deberiaRetornarListaDeOrdenes() {
        // Given
        Orden orden1 = new Orden();
        orden1.setId(1L);
        orden1.setUsuarioId(1L);
        orden1.setProductoId(1L);
        orden1.setCantidad(2);

        Orden orden2 = new Orden();
        orden2.setId(2L);
        orden2.setUsuarioId(2L);
        orden2.setProductoId(2L);
        orden2.setCantidad(1);

        when(ordenRepository.findAll()).thenReturn(Arrays.asList(orden1, orden2));

        // When
        List<Orden> resultado = ordenService.listarTodas();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getUsuarioId());
        assertEquals(2L, resultado.get(1).getUsuarioId());

        verify(ordenRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarOrden() {
        // Given
        Long id = 1L;

        Orden orden = new Orden();
        orden.setId(id);
        orden.setUsuarioId(1L);
        orden.setProductoId(1L);
        orden.setCantidad(2);

        when(ordenRepository.findById(id)).thenReturn(Optional.of(orden));

        // When
        Orden resultado = ordenService.buscarPorId(id);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(1L, resultado.getUsuarioId());
        assertEquals(1L, resultado.getProductoId());
        assertEquals(2, resultado.getCantidad());

        verify(ordenRepository, times(1)).findById(id);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        Long id = 99L;

        when(ordenRepository.findById(id)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ordenService.buscarPorId(id);
        });

        // Then
        assertEquals("Orden no encontrada con ID: 99", exception.getMessage());

        verify(ordenRepository, times(1)).findById(id);
    }

    @Test
    void guardar_conOrdenValida_deberiaGuardarOrdenCorrectamente() {
        // Given
        ReflectionTestUtils.setField(ordenService, "usuarioServiceUrl", "http://localhost:8081");
        ReflectionTestUtils.setField(ordenService, "productoServiceUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(ordenService, "inventarioServiceUrl", "http://localhost:8083");

        Orden orden = new Orden();
        orden.setUsuarioId(1L);
        orden.setProductoId(1L);
        orden.setCantidad(2);

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("Javier");
        usuarioDTO.setCorreo("javier@gmail.com");
        usuarioDTO.setRut("12345678-9");

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setNombre("Mouse Gamer");
        productoDTO.setCategoria("Perifericos");
        productoDTO.setPrecio(25000.0);
        productoDTO.setStock(10);

        InventarioDTO inventarioDTO = new InventarioDTO();
        inventarioDTO.setId(1L);
        inventarioDTO.setProductoId(1L);
        inventarioDTO.setStockActual(10);
        inventarioDTO.setUbicacionBodega("Bodega Central");

        Orden ordenGuardada = new Orden();
        ordenGuardada.setId(1L);
        ordenGuardada.setUsuarioId(1L);
        ordenGuardada.setProductoId(1L);
        ordenGuardada.setCantidad(2);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UsuarioDTO.class)).thenReturn(Mono.just(usuarioDTO));
        when(responseSpec.bodyToMono(ProductoDTO.class)).thenReturn(Mono.just(productoDTO));
        when(responseSpec.bodyToMono(InventarioDTO.class)).thenReturn(Mono.just(inventarioDTO));
        when(ordenRepository.save(orden)).thenReturn(ordenGuardada);

        // When
        Orden resultado = ordenService.guardar(orden);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getUsuarioId());
        assertEquals(1L, resultado.getProductoId());
        assertEquals(2, resultado.getCantidad());
        assertNotNull(orden.getFechaOrden());

        verify(ordenRepository, times(1)).save(orden);
    }

    @Test
    void guardar_conUsuarioIdNulo_deberiaLanzarExcepcion() {
        // Given
        Orden orden = new Orden();
        orden.setUsuarioId(null);
        orden.setProductoId(1L);
        orden.setCantidad(2);

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ordenService.guardar(orden);
        });

        // Then
        assertEquals("El usuarioId es obligatorio", exception.getMessage());

        verify(ordenRepository, never()).save(any(Orden.class));
    }

    @Test
    void guardar_conProductoIdNulo_deberiaLanzarExcepcion() {
        // Given
        Orden orden = new Orden();
        orden.setUsuarioId(1L);
        orden.setProductoId(null);
        orden.setCantidad(2);

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ordenService.guardar(orden);
        });

        // Then
        assertEquals("El productoId es obligatorio", exception.getMessage());

        verify(ordenRepository, never()).save(any(Orden.class));
    }

    @Test
    void guardar_conCantidadInvalida_deberiaLanzarExcepcion() {
        // Given
        Orden orden = new Orden();
        orden.setUsuarioId(1L);
        orden.setProductoId(1L);
        orden.setCantidad(0);

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ordenService.guardar(orden);
        });

        // Then
        assertEquals("La cantidad debe ser mayor a 0", exception.getMessage());

        verify(ordenRepository, never()).save(any(Orden.class));
    }

    @Test
    void guardar_conStockInsuficiente_deberiaLanzarExcepcion() {
        // Given
        ReflectionTestUtils.setField(ordenService, "usuarioServiceUrl", "http://localhost:8081");
        ReflectionTestUtils.setField(ordenService, "productoServiceUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(ordenService, "inventarioServiceUrl", "http://localhost:8083");

        Orden orden = new Orden();
        orden.setUsuarioId(1L);
        orden.setProductoId(1L);
        orden.setCantidad(10);

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("Javier");

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setNombre("Mouse Gamer");

        InventarioDTO inventarioDTO = new InventarioDTO();
        inventarioDTO.setId(1L);
        inventarioDTO.setProductoId(1L);
        inventarioDTO.setStockActual(2);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UsuarioDTO.class)).thenReturn(Mono.just(usuarioDTO));
        when(responseSpec.bodyToMono(ProductoDTO.class)).thenReturn(Mono.just(productoDTO));
        when(responseSpec.bodyToMono(InventarioDTO.class)).thenReturn(Mono.just(inventarioDTO));

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ordenService.guardar(orden);
        });

        // Then
        assertEquals("Stock insuficiente para crear la orden", exception.getMessage());

        verify(ordenRepository, never()).save(any(Orden.class));
    }

    @Test
    void eliminar_cuandoExiste_deberiaEliminarOrden() {
        // Given
        Long id = 1L;

        when(ordenRepository.existsById(id)).thenReturn(true);
        doNothing().when(ordenRepository).deleteById(id);

        // When
        ordenService.eliminar(id);

        // Then
        verify(ordenRepository, times(1)).existsById(id);
        verify(ordenRepository, times(1)).deleteById(id);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        Long id = 99L;

        when(ordenRepository.existsById(id)).thenReturn(false);

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ordenService.eliminar(id);
        });

        // Then
        assertEquals("Orden no encontrada con ID: 99", exception.getMessage());

        verify(ordenRepository, times(1)).existsById(id);
        verify(ordenRepository, never()).deleteById(id);
    }

    @Test
    void eliminar_conIdNulo_deberiaLanzarExcepcion() {
        // Given
        Long id = null;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ordenService.eliminar(id);
        });

        // Then
        assertEquals("El ID de la orden es obligatorio", exception.getMessage());

        verify(ordenRepository, never()).existsById(any());
        verify(ordenRepository, never()).deleteById(any());
    }
}
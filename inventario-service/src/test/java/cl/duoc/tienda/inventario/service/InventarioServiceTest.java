package cl.duoc.tienda.inventario.service;

import cl.duoc.tienda.inventario.model.Inventario;
import cl.duoc.tienda.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void listarTodos_deberiaRetornarListaDeInventario() {
        // Given
        Inventario inventario1 = new Inventario();
        inventario1.setId(1L);
        inventario1.setProductoId(1L);
        inventario1.setStockActual(10);
        inventario1.setUbicacionBodega("Bodega Central");

        Inventario inventario2 = new Inventario();
        inventario2.setId(2L);
        inventario2.setProductoId(2L);
        inventario2.setStockActual(5);
        inventario2.setUbicacionBodega("Bodega Secundaria");

        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(inventario1, inventario2));

        // When
        List<Inventario> resultado = inventarioService.listarTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getProductoId());
        assertEquals(2L, resultado.get(1).getProductoId());

        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarInventario() {
        // Given
        Long id = 1L;

        Inventario inventario = new Inventario();
        inventario.setId(id);
        inventario.setProductoId(1L);
        inventario.setStockActual(10);
        inventario.setUbicacionBodega("Bodega Central");

        when(inventarioRepository.findById(id)).thenReturn(Optional.of(inventario));

        // When
        Inventario resultado = inventarioService.buscarPorId(id);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(1L, resultado.getProductoId());
        assertEquals(10, resultado.getStockActual());
        assertEquals("Bodega Central", resultado.getUbicacionBodega());

        verify(inventarioRepository, times(1)).findById(id);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        Long id = 99L;

        when(inventarioRepository.findById(id)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventarioService.buscarPorId(id);
        });

        // Then
        assertEquals("Inventario no encontrado con ID: 99", exception.getMessage());

        verify(inventarioRepository, times(1)).findById(id);
    }

    @Test
    void buscarPorProductoId_cuandoExiste_deberiaRetornarInventario() {
        // Given
        Long productoId = 1L;

        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProductoId(productoId);
        inventario.setStockActual(10);
        inventario.setUbicacionBodega("Bodega Central");

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventario));

        // When
        Inventario resultado = inventarioService.buscarPorProductoId(productoId);

        // Then
        assertNotNull(resultado);
        assertEquals(productoId, resultado.getProductoId());
        assertEquals(10, resultado.getStockActual());

        verify(inventarioRepository, times(1)).findByProductoId(productoId);
    }

    @Test
    void buscarPorProductoId_cuandoProductoIdEsNulo_deberiaLanzarExcepcion() {
        // Given
        Long productoId = null;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.buscarPorProductoId(productoId);
        });

        // Then
        assertEquals("El productoId es obligatorio", exception.getMessage());

        verify(inventarioRepository, never()).findByProductoId(any());
    }

    @Test
    void buscarPorProductoId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        Long productoId = 99L;

        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventarioService.buscarPorProductoId(productoId);
        });

        // Then
        assertEquals("Inventario no encontrado para producto ID: 99", exception.getMessage());

        verify(inventarioRepository, times(1)).findByProductoId(productoId);
    }

    @Test
    void guardar_conInventarioValido_deberiaGuardarInventarioCorrectamente() {
        // Given
        Inventario inventario = new Inventario();
        inventario.setProductoId(1L);
        inventario.setStockActual(10);
        inventario.setUbicacionBodega("Bodega Central");

        Inventario inventarioGuardado = new Inventario();
        inventarioGuardado.setId(1L);
        inventarioGuardado.setProductoId(1L);
        inventarioGuardado.setStockActual(10);
        inventarioGuardado.setUbicacionBodega("Bodega Central");

        when(inventarioRepository.save(inventario)).thenReturn(inventarioGuardado);

        // When
        Inventario resultado = inventarioService.guardar(inventario);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getProductoId());
        assertEquals(10, resultado.getStockActual());
        assertEquals("Bodega Central", resultado.getUbicacionBodega());

        verify(inventarioRepository, times(1)).save(inventario);
    }

    @Test
    void guardar_conProductoIdNulo_deberiaLanzarExcepcion() {
        // Given
        Inventario inventario = new Inventario();
        inventario.setProductoId(null);
        inventario.setStockActual(10);
        inventario.setUbicacionBodega("Bodega Central");

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.guardar(inventario);
        });

        // Then
        assertEquals("El productoId es obligatorio", exception.getMessage());

        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void guardar_conStockNegativo_deberiaLanzarExcepcion() {
        // Given
        Inventario inventario = new Inventario();
        inventario.setProductoId(1L);
        inventario.setStockActual(-1);
        inventario.setUbicacionBodega("Bodega Central");

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.guardar(inventario);
        });

        // Then
        assertEquals("El stock actual no puede ser negativo", exception.getMessage());

        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void guardar_conUbicacionVacia_deberiaLanzarExcepcion() {
        // Given
        Inventario inventario = new Inventario();
        inventario.setProductoId(1L);
        inventario.setStockActual(10);
        inventario.setUbicacionBodega("");

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.guardar(inventario);
        });

        // Then
        assertEquals("La ubicación de bodega es obligatoria", exception.getMessage());

        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void eliminar_cuandoExiste_deberiaEliminarInventario() {
        // Given
        Long id = 1L;

        when(inventarioRepository.existsById(id)).thenReturn(true);
        doNothing().when(inventarioRepository).deleteById(id);

        // When
        inventarioService.eliminar(id);

        // Then
        verify(inventarioRepository, times(1)).existsById(id);
        verify(inventarioRepository, times(1)).deleteById(id);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        Long id = 99L;

        when(inventarioRepository.existsById(id)).thenReturn(false);

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventarioService.eliminar(id);
        });

        // Then
        assertEquals("Inventario no encontrado con ID: 99", exception.getMessage());

        verify(inventarioRepository, times(1)).existsById(id);
        verify(inventarioRepository, never()).deleteById(id);
    }

    @Test
    void eliminar_conIdNulo_deberiaLanzarExcepcion() {
        // Given
        Long id = null;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventarioService.eliminar(id);
        });

        // Then
        assertEquals("El ID del inventario es obligatorio", exception.getMessage());

        verify(inventarioRepository, never()).existsById(any());
        verify(inventarioRepository, never()).deleteById(any());
    }
}
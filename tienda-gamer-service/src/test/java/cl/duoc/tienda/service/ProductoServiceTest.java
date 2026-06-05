package cl.duoc.tienda.service;

import cl.duoc.tienda.model.Producto;
import cl.duoc.tienda.repository.ProductoRepository;
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
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void listarTodos_deberiaRetornarListaDeProductos() {
        // Given
        Producto producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Mouse Gamer");
        producto1.setCategoria("Perifericos");
        producto1.setPrecio(25000.0);
        producto1.setStock(10);

        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Teclado Mecanico");
        producto2.setCategoria("Perifericos");
        producto2.setPrecio(45000.0);
        producto2.setStock(5);

        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto1, producto2));

        // When
        List<Producto> resultado = productoService.listarTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Mouse Gamer", resultado.get(0).getNombre());
        assertEquals("Teclado Mecanico", resultado.get(1).getNombre());

        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarProducto() {
        // Given
        Long id = 1L;

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Mouse Gamer");
        producto.setCategoria("Perifericos");
        producto.setPrecio(25000.0);
        producto.setStock(10);

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        // When
        Producto resultado = productoService.buscarPorId(id);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Mouse Gamer", resultado.getNombre());
        assertEquals(25000.0, resultado.getPrecio());
        assertEquals(10, resultado.getStock());

        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        Long id = 99L;

        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.buscarPorId(id);
        });

        // Then
        assertEquals("Producto no encontrado con ID: 99", exception.getMessage());

        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void guardar_conProductoValido_deberiaGuardarProductoCorrectamente() {
        // Given
        Producto producto = new Producto();
        producto.setNombre("Mouse Gamer");
        producto.setCategoria("Perifericos");
        producto.setPrecio(25000.0);
        producto.setStock(10);

        Producto productoGuardado = new Producto();
        productoGuardado.setId(1L);
        productoGuardado.setNombre("Mouse Gamer");
        productoGuardado.setCategoria("Perifericos");
        productoGuardado.setPrecio(25000.0);
        productoGuardado.setStock(10);

        when(productoRepository.save(producto)).thenReturn(productoGuardado);

        // When
        Producto resultado = productoService.guardar(producto);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Mouse Gamer", resultado.getNombre());
        assertEquals(25000.0, resultado.getPrecio());
        assertEquals(10, resultado.getStock());

        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void guardar_conNombreVacio_deberiaLanzarExcepcion() {
        // Given
        Producto producto = new Producto();
        producto.setNombre("");
        producto.setCategoria("Perifericos");
        producto.setPrecio(25000.0);
        producto.setStock(10);

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productoService.guardar(producto);
        });

        // Then
        assertEquals("El nombre del producto es obligatorio", exception.getMessage());

        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void guardar_conPrecioInvalido_deberiaLanzarExcepcion() {
        // Given
        Producto producto = new Producto();
        producto.setNombre("Mouse Gamer");
        producto.setCategoria("Perifericos");
        producto.setPrecio(0.0);
        producto.setStock(10);

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productoService.guardar(producto);
        });

        // Then
        assertEquals("El precio debe ser mayor a 0", exception.getMessage());

        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void guardar_conStockNegativo_deberiaLanzarExcepcion() {
        // Given
        Producto producto = new Producto();
        producto.setNombre("Mouse Gamer");
        producto.setCategoria("Perifericos");
        producto.setPrecio(25000.0);
        producto.setStock(-1);

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productoService.guardar(producto);
        });

        // Then
        assertEquals("El stock no puede ser negativo", exception.getMessage());

        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void eliminar_cuandoExiste_deberiaEliminarProducto() {
        // Given
        Long id = 1L;

        when(productoRepository.existsById(id)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(id);

        // When
        productoService.eliminar(id);

        // Then
        verify(productoRepository, times(1)).existsById(id);
        verify(productoRepository, times(1)).deleteById(id);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        Long id = 99L;

        when(productoRepository.existsById(id)).thenReturn(false);

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.eliminar(id);
        });

        // Then
        assertEquals("Producto no encontrado con ID: 99", exception.getMessage());

        verify(productoRepository, times(1)).existsById(id);
        verify(productoRepository, never()).deleteById(id);
    }
}
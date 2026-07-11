package cl.duoc.tienda.carrito.service;

import cl.duoc.tienda.carrito.dto.CarritoRequest;
import cl.duoc.tienda.carrito.model.Carrito;
import cl.duoc.tienda.carrito.repository.CarritoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.NoSuchElementException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {
    @Mock CarritoRepository repository;
    @InjectMocks CarritoService service;

    @Test
    void guardar_mantieneUsuarioProductoYCantidad() {
        Carrito guardado = new Carrito(1L, 10L, 2);
        guardado.setId(1L);
        when(repository.save(any(Carrito.class))).thenReturn(guardado);

        Carrito resultado = service.guardar(new CarritoRequest(1L, 10L, 2));

        assertEquals(10L, resultado.getProductoId());
        assertEquals(2, resultado.getCantidad());
        verify(repository).save(any(Carrito.class));
    }

    @Test
    void buscar_cuandoNoExisteLanzaErrorDeDominio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertEquals("Carrito no encontrado: 99",
                assertThrows(NoSuchElementException.class, () -> service.buscar(99L)).getMessage());
    }

    @Test
    void actualizar_verificaExistenciaYConservaId() {
        when(repository.findById(3L)).thenReturn(Optional.of(new Carrito(1L, 1L, 1)));
        when(repository.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Carrito resultado = service.actualizar(3L, new CarritoRequest(2L, 11L, 4));

        assertEquals(3L, resultado.getId());
        assertEquals(4, resultado.getCantidad());
    }

    @Test
    void listarYEliminar_deleganEnElRepositorio() {
        when(repository.findAll()).thenReturn(List.of(new Carrito(1L, 1L, 1)));
        when(repository.findById(6L)).thenReturn(Optional.of(new Carrito(1L, 1L, 1)));

        assertEquals(1, service.listar().size());
        service.eliminar(6L);

        verify(repository).findAll();
        verify(repository).deleteById(6L);
    }
}

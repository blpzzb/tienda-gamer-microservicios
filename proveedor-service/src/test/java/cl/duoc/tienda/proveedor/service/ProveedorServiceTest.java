package cl.duoc.tienda.proveedor.service;

import cl.duoc.tienda.proveedor.dto.ProveedorRequest;
import cl.duoc.tienda.proveedor.model.Proveedor;
import cl.duoc.tienda.proveedor.repository.ProveedorRepository;
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
class ProveedorServiceTest {
    @Mock ProveedorRepository repository;
    @InjectMocks ProveedorService service;

    @Test
    void guardar_mapeaProveedorYLoPersiste() {
        Proveedor guardado = new Proveedor("Gamer Supply", "ventas@gamer.cl");
        guardado.setId(1L);
        when(repository.save(any(Proveedor.class))).thenReturn(guardado);

        Proveedor resultado = service.guardar(new ProveedorRequest("Gamer Supply", "ventas@gamer.cl"));

        assertEquals("ventas@gamer.cl", resultado.getEmail());
        verify(repository).save(any(Proveedor.class));
    }

    @Test
    void buscar_cuandoNoExisteLanzaErrorDeDominio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertEquals("Proveedor no encontrado: 99",
                assertThrows(NoSuchElementException.class, () -> service.buscar(99L)).getMessage());
    }

    @Test
    void eliminar_buscaAntesDeEliminar() {
        when(repository.findById(2L)).thenReturn(Optional.of(new Proveedor("P", "p@p.cl")));

        service.eliminar(2L);

        verify(repository).deleteById(2L);
    }

    @Test
    void listarYActualizar_deleganEnElRepositorio() {
        when(repository.findAll()).thenReturn(List.of(new Proveedor("Gamer Supply", "ventas@gamer.cl")));
        when(repository.findById(5L)).thenReturn(Optional.of(new Proveedor("Anterior", "a@a.cl")));
        when(repository.save(any(Proveedor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals(1, service.listar().size());
        Proveedor resultado = service.actualizar(5L, new ProveedorRequest("Nuevo", "nuevo@duoc.cl"));

        assertEquals(5L, resultado.getId());
        verify(repository).findAll();
    }
}

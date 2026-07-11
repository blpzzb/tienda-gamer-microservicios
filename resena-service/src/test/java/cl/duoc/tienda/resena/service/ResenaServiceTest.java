package cl.duoc.tienda.resena.service;

import cl.duoc.tienda.resena.dto.ResenaRequest;
import cl.duoc.tienda.resena.model.Resena;
import cl.duoc.tienda.resena.repository.ResenaRepository;
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
class ResenaServiceTest {
    @Mock ResenaRepository repository;
    @InjectMocks ResenaService service;

    @Test
    void guardar_mantieneCalificacionYComentario() {
        Resena guardada = new Resena(1L, 10L, 5, "Excelente");
        guardada.setId(1L);
        when(repository.save(any(Resena.class))).thenReturn(guardada);

        Resena resultado = service.guardar(new ResenaRequest(1L, 10L, 5, "Excelente"));

        assertEquals(5, resultado.getCalificacion());
        assertEquals("Excelente", resultado.getComentario());
        verify(repository).save(any(Resena.class));
    }

    @Test
    void buscar_cuandoNoExisteLanzaErrorDeDominio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertEquals("Resena no encontrado: 99",
                assertThrows(NoSuchElementException.class, () -> service.buscar(99L)).getMessage());
    }

    @Test
    void eliminar_buscaAntesDeEliminar() {
        when(repository.findById(4L)).thenReturn(Optional.of(new Resena(1L, 2L, 4, "Buena")));

        service.eliminar(4L);

        verify(repository).deleteById(4L);
    }

    @Test
    void listarYActualizar_deleganEnElRepositorio() {
        when(repository.findAll()).thenReturn(List.of(new Resena(1L, 2L, 5, "Excelente")));
        when(repository.findById(7L)).thenReturn(Optional.of(new Resena(1L, 2L, 4, "Buena")));
        when(repository.save(any(Resena.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals(1, service.listar().size());
        Resena resultado = service.actualizar(7L, new ResenaRequest(1L, 2L, 5, "Mejor"));

        assertEquals(7L, resultado.getId());
        verify(repository).findAll();
    }
}

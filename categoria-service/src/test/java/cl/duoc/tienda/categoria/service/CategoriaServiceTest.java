package cl.duoc.tienda.categoria.service;

import cl.duoc.tienda.categoria.dto.CategoriaRequest;
import cl.duoc.tienda.categoria.model.Categoria;
import cl.duoc.tienda.categoria.repository.CategoriaRepository;
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
class CategoriaServiceTest {
    @Mock CategoriaRepository repository;
    @InjectMocks CategoriaService service;

    @Test
    void guardar_mapeaElRequestYLoPersiste() {
        Categoria guardada = new Categoria("Consolas", "Videojuegos");
        guardada.setId(1L);
        when(repository.save(any(Categoria.class))).thenReturn(guardada);

        Categoria resultado = service.guardar(new CategoriaRequest("Consolas", "Videojuegos"));

        assertEquals(1L, resultado.getId());
        assertEquals("Consolas", resultado.getNombre());
        verify(repository).save(any(Categoria.class));
    }

    @Test
    void buscar_cuandoNoExisteInformaElId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        NoSuchElementException error = assertThrows(NoSuchElementException.class, () -> service.buscar(99L));

        assertEquals("Categoria no encontrado: 99", error.getMessage());
    }

    @Test
    void actualizar_verificaExistenciaAntesDeGuardar() {
        Categoria existente = new Categoria("Anterior", "Anterior");
        existente.setId(5L);
        when(repository.findById(5L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Categoria.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Categoria resultado = service.actualizar(5L, new CategoriaRequest("Audio Gamer", "Audifonos y parlantes"));

        assertEquals(5L, resultado.getId());
        assertEquals("Audio Gamer", resultado.getNombre());
    }

    @Test
    void listarYEliminar_deleganEnElRepositorio() {
        when(repository.findAll()).thenReturn(List.of(new Categoria("Juegos", "Digitales")));
        when(repository.findById(9L)).thenReturn(Optional.of(new Categoria("Juegos", "Digitales")));

        assertEquals(1, service.listar().size());
        service.eliminar(9L);

        verify(repository).findAll();
        verify(repository).deleteById(9L);
    }
}

package cl.duoc.tienda.notificacion.service;

import cl.duoc.tienda.notificacion.dto.NotificacionRequest;
import cl.duoc.tienda.notificacion.model.Notificacion;
import cl.duoc.tienda.notificacion.repository.NotificacionRepository;
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
class NotificacionServiceTest {
    @Mock NotificacionRepository repository;
    @InjectMocks NotificacionService service;

    @Test
    void guardar_mantieneDestinatarioMensajeYEstado() {
        Notificacion guardada = new Notificacion(1L, "Pago aprobado", false);
        guardada.setId(1L);
        when(repository.save(any(Notificacion.class))).thenReturn(guardada);

        Notificacion resultado = service.guardar(new NotificacionRequest(1L, "Pago aprobado", false));

        assertFalse(resultado.getLeida());
        assertEquals("Pago aprobado", resultado.getMensaje());
        verify(repository).save(any(Notificacion.class));
    }

    @Test
    void buscar_cuandoNoExisteLanzaErrorDeDominio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertEquals("Notificacion no encontrado: 99",
                assertThrows(NoSuchElementException.class, () -> service.buscar(99L)).getMessage());
    }

    @Test
    void actualizar_verificaExistenciaYConservaId() {
        when(repository.findById(8L)).thenReturn(Optional.of(new Notificacion(1L, "Antes", true)));
        when(repository.save(any(Notificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Notificacion resultado = service.actualizar(8L, new NotificacionRequest(2L, "Nueva orden", false));

        assertEquals(8L, resultado.getId());
        assertFalse(resultado.getLeida());
    }

    @Test
    void listarYEliminar_deleganEnElRepositorio() {
        when(repository.findAll()).thenReturn(List.of(new Notificacion(1L, "Pago aprobado", false)));
        when(repository.findById(4L)).thenReturn(Optional.of(new Notificacion(1L, "Pago aprobado", false)));

        assertEquals(1, service.listar().size());
        service.eliminar(4L);

        verify(repository).findAll();
        verify(repository).deleteById(4L);
    }
}

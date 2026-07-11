package cl.duoc.tienda.usuario.controller;

import cl.duoc.tienda.usuario.exception.ApiExceptionHandler;
import cl.duoc.tienda.usuario.model.Usuario;
import cl.duoc.tienda.usuario.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioWebLayerTest {
    private final UsuarioService service = mock(UsuarioService.class);
    private final UsuarioController controller = new UsuarioController();

    @BeforeEach
    void setUp() throws Exception { inject(controller, "usuarioService", service); }

    @Test
    void controllerDelegatesCrudOperations() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(service.listarTodos()).thenReturn(List.of(usuario));
        when(service.buscarPorId(1L)).thenReturn(usuario);
        when(service.guardar(any())).thenReturn(usuario);
        when(service.actualizar(eq(1L), any())).thenReturn(usuario);

        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(usuario, controller.getById(1L).getBody());
        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(usuario, controller.update(1L, null).getBody());
        assertEquals(HttpStatus.NO_CONTENT, controller.delete(1L).getStatusCode());
        verify(service).eliminar(1L);
    }

    @Test
    void exceptionHandlerMapsCommonErrors() {
        ApiExceptionHandler handler = new ApiExceptionHandler();
        assertEquals(HttpStatus.BAD_REQUEST, handler.handleIllegalArgument(new IllegalArgumentException("dato inválido")).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, handler.handleNoSuchElement(new NoSuchElementException("no existe")).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, handler.handleRuntime(new RuntimeException("fallo")).getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, handler.handleGeneric(new Exception("error")).getStatusCode());
    }

    private static void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}

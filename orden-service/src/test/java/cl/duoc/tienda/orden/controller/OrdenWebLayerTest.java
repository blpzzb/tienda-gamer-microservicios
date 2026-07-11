package cl.duoc.tienda.orden.controller;

import cl.duoc.tienda.orden.exception.ApiExceptionHandler;
import cl.duoc.tienda.orden.model.Orden;
import cl.duoc.tienda.orden.service.OrdenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrdenWebLayerTest {
    private final OrdenService service = mock(OrdenService.class);
    private final OrdenController controller = new OrdenController();

    @BeforeEach
    void setUp() throws Exception { inject(controller, "ordenService", service); }

    @Test
    void controllerDelegatesCrudOperations() {
        Orden orden = new Orden();
        orden.setId(1L);
        when(service.listarTodas()).thenReturn(List.of(orden));
        when(service.buscarPorId(1L)).thenReturn(orden);
        when(service.guardar(any())).thenReturn(orden);
        when(service.actualizar(eq(1L), any())).thenReturn(orden);

        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(orden, controller.getById(1L).getBody());
        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(orden, controller.update(1L, null).getBody());
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

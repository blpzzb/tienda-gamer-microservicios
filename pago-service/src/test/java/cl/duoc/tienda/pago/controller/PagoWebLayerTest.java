package cl.duoc.tienda.pago.controller;

import cl.duoc.tienda.pago.exception.ApiExceptionHandler;
import cl.duoc.tienda.pago.model.Pago;
import cl.duoc.tienda.pago.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PagoWebLayerTest {
    private final PagoService service = mock(PagoService.class);
    private final PagoController controller = new PagoController();

    @BeforeEach
    void setUp() throws Exception { inject(controller, "pagoService", service); }

    @Test
    void controllerDelegatesCrudOperations() {
        Pago pago = new Pago();
        pago.setId(1L);
        when(service.listarTodos()).thenReturn(List.of(pago));
        when(service.buscarPorId(1L)).thenReturn(pago);
        when(service.guardar(any())).thenReturn(pago);
        when(service.actualizar(eq(1L), any())).thenReturn(pago);

        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(pago, controller.getById(1L).getBody());
        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(pago, controller.update(1L, null).getBody());
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

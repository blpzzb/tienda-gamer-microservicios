package cl.duoc.tienda.notificacion.controller;

import cl.duoc.tienda.notificacion.exception.ApiExceptionHandler;
import cl.duoc.tienda.notificacion.model.Notificacion;
import cl.duoc.tienda.notificacion.service.NotificacionService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificacionWebLayerTest {
    @Test void controllerDelegatesCrudOperations() {
        NotificacionService service = mock(NotificacionService.class); Notificacion item = new Notificacion(1L, "Pedido enviado", false); item.setId(1L);
        when(service.listar()).thenReturn(List.of(item)); when(service.buscar(1L)).thenReturn(item); when(service.guardar(any())).thenReturn(item); when(service.actualizar(eq(1L), any())).thenReturn(item);
        NotificacionController controller = new NotificacionController(service);
        assertEquals(HttpStatus.OK, controller.listar().getStatusCode()); assertEquals(item, controller.buscar(1L).getBody()); assertEquals(HttpStatus.CREATED, controller.crear(null).getStatusCode()); assertEquals(item, controller.actualizar(1L, null).getBody()); assertEquals(HttpStatus.NO_CONTENT, controller.eliminar(1L).getStatusCode()); verify(service).eliminar(1L);
    }
    @Test void exceptionHandlerMapsCommonErrors() {
        ApiExceptionHandler handler = new ApiExceptionHandler(); assertEquals(HttpStatus.BAD_REQUEST, handler.handleIllegalArgument(new IllegalArgumentException("dato")).getStatusCode()); assertEquals(HttpStatus.NOT_FOUND, handler.handleNoSuchElement(new NoSuchElementException("no existe")).getStatusCode()); assertEquals(HttpStatus.NOT_FOUND, handler.handleRuntime(new RuntimeException("fallo")).getStatusCode()); assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, handler.handleGeneric(new Exception("error")).getStatusCode());
    }
    @Test void exceptionHandlerMapsValidationErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class); BindingResult result = mock(BindingResult.class); when(exception.getBindingResult()).thenReturn(result); when(result.getFieldErrors()).thenReturn(List.of(new FieldError("notificacion", "mensaje", "requerido")));
        assertEquals(HttpStatus.BAD_REQUEST, new ApiExceptionHandler().handleValidation(exception).getStatusCode());
    }
}

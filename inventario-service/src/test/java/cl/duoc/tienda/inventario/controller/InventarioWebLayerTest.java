package cl.duoc.tienda.inventario.controller;

import cl.duoc.tienda.inventario.exception.ApiExceptionHandler;
import cl.duoc.tienda.inventario.model.Inventario;
import cl.duoc.tienda.inventario.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventarioWebLayerTest {
    private final InventarioService service = mock(InventarioService.class);
    private final InventarioController controller = new InventarioController();

    @BeforeEach
    void setUp() throws Exception { inject(controller, "inventarioService", service); }

    @Test
    void controllerDelegatesCrudOperations() {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        when(service.listarTodos()).thenReturn(List.of(inventario));
        when(service.buscarPorId(1L)).thenReturn(inventario);
        when(service.buscarPorProductoId(10L)).thenReturn(inventario);
        when(service.guardar(any())).thenReturn(inventario);
        when(service.actualizar(eq(1L), any())).thenReturn(inventario);

        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(inventario, controller.getById(1L).getBody());
        assertEquals(inventario, controller.getByProductoId(10L).getBody());
        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(inventario, controller.update(1L, null).getBody());
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

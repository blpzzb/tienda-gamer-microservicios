package cl.duoc.tienda.controller;

import cl.duoc.tienda.exception.ApiExceptionHandler;
import cl.duoc.tienda.model.Producto;
import cl.duoc.tienda.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductoWebLayerTest {
    private final ProductoService service = mock(ProductoService.class);
    private final ProductoController controller = new ProductoController();

    @BeforeEach
    void setUp() throws Exception { inject(controller, "productoService", service); }

    @Test
    void controllerDelegatesCrudOperations() {
        Producto producto = new Producto();
        producto.setId(1L);
        when(service.listarTodos()).thenReturn(List.of(producto));
        when(service.buscarPorId(1L)).thenReturn(producto);
        when(service.guardar(any())).thenReturn(producto);
        when(service.actualizar(eq(1L), any())).thenReturn(producto);

        assertEquals(HttpStatus.OK, controller.getAll().getStatusCode());
        assertEquals(producto, controller.getById(1L).getBody());
        assertEquals(HttpStatus.CREATED, controller.create(null).getStatusCode());
        assertEquals(producto, controller.update(1L, null).getBody());
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

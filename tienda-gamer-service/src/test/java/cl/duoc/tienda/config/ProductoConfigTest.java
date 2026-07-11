package cl.duoc.tienda.config;

import cl.duoc.tienda.repository.ProductoRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductoConfigTest {
    @Test
    void exposesOpenApiMetadata() {
        assertEquals("Tienda Gamer Service API", new OpenApiConfig().tiendaGamerServiceOpenAPI().getInfo().getTitle());
    }

    @Test
    void seedsDataOnlyWhenRepositoryIsEmpty() throws Exception {
        ProductoRepository repository = mock(ProductoRepository.class);
        when(repository.count()).thenReturn(0L);
        new ProductoDataInitializer().cargarProductosDePrueba(repository).run();
        verify(repository, times(5)).save(any());

        reset(repository);
        when(repository.count()).thenReturn(1L);
        new ProductoDataInitializer().cargarProductosDePrueba(repository).run();
        verify(repository, never()).save(any());
    }
}

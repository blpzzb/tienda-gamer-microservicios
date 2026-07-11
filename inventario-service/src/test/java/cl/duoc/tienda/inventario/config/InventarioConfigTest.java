package cl.duoc.tienda.inventario.config;

import cl.duoc.tienda.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventarioConfigTest {
    @Test
    void exposesOpenApiMetadata() {
        assertEquals("Inventario Service API", new OpenApiConfig().inventarioServiceOpenAPI().getInfo().getTitle());
    }

    @Test
    void seedsDataOnlyWhenRepositoryIsEmpty() throws Exception {
        InventarioRepository repository = mock(InventarioRepository.class);
        when(repository.count()).thenReturn(0L);
        new InventarioDataInitializer().cargarInventarioDePrueba(repository).run();
        verify(repository, times(5)).save(any());

        reset(repository);
        when(repository.count()).thenReturn(1L);
        new InventarioDataInitializer().cargarInventarioDePrueba(repository).run();
        verify(repository, never()).save(any());
    }
}

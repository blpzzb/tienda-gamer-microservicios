package cl.duoc.tienda.usuario.config;

import cl.duoc.tienda.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UsuarioConfigTest {
    @Test
    void exposesOpenApiMetadata() {
        assertEquals("Usuario Service API", new OpenApiConfig().usuarioServiceOpenAPI().getInfo().getTitle());
    }

    @Test
    void seedsDataOnlyWhenRepositoryIsEmpty() throws Exception {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        when(repository.count()).thenReturn(0L);
        new UsuarioDataInitializer().cargarUsuariosDePrueba(repository).run();
        verify(repository, times(5)).save(any());

        reset(repository);
        when(repository.count()).thenReturn(1L);
        new UsuarioDataInitializer().cargarUsuariosDePrueba(repository).run();
        verify(repository, never()).save(any());
    }
}

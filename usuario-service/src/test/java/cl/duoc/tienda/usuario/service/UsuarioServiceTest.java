package cl.duoc.tienda.usuario.service;

import cl.duoc.tienda.usuario.model.Usuario;
import cl.duoc.tienda.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void listarTodos_deberiaRetornarListaDeUsuarios() {
        // Given
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Javier");
        usuario1.setCorreo("javier@gmail.com");
        usuario1.setRut("12345678-9");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("Carlos");
        usuario2.setCorreo("carlos@gmail.com");
        usuario2.setRut("98765432-1");

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));

        // When
        List<Usuario> resultado = usuarioService.listarTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Javier", resultado.get(0).getNombre());
        assertEquals("Carlos", resultado.get(1).getNombre());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void guardar_deberiaGuardarUsuarioCorrectamente() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNombre("Javier");
        usuario.setCorreo("javier@gmail.com");
        usuario.setRut("12345678-9");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(1L);
        usuarioGuardado.setNombre("Javier");
        usuarioGuardado.setCorreo("javier@gmail.com");
        usuarioGuardado.setRut("12345678-9");

        when(usuarioRepository.save(usuario)).thenReturn(usuarioGuardado);

        // When
        Usuario resultado = usuarioService.guardar(usuario);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Javier", resultado.getNombre());
        assertEquals("javier@gmail.com", resultado.getCorreo());
        assertEquals("12345678-9", resultado.getRut());

        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarUsuario() {
        // Given
        Long id = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Javier");
        usuario.setCorreo("javier@gmail.com");
        usuario.setRut("12345678-9");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // When
        Usuario resultado = usuarioService.buscarPorId(id);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Javier", resultado.getNombre());
        assertEquals("javier@gmail.com", resultado.getCorreo());

        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        Long id = 99L;

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarPorId(id);
        });

        // Then
        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());

        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    void eliminar_deberiaEliminarUsuarioPorId() {
        // Given
        Long id = 1L;

        doNothing().when(usuarioRepository).deleteById(id);

        // When
        usuarioService.eliminar(id);

        // Then
        verify(usuarioRepository, times(1)).deleteById(id);
    }
}
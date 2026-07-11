package cl.duoc.tienda.usuario.service;

import cl.duoc.tienda.usuario.dto.UsuarioRequest;
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
        UsuarioRequest request = new UsuarioRequest("Javier", "javier@gmail.com", "12345678-9");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(1L);
        usuarioGuardado.setNombre("Javier");
        usuarioGuardado.setCorreo("javier@gmail.com");
        usuarioGuardado.setRut("12345678-9");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        // When
        Usuario resultado = usuarioService.guardar(request);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Javier", resultado.getNombre());
        assertEquals("javier@gmail.com", resultado.getCorreo());
        assertEquals("12345678-9", resultado.getRut());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
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

        when(usuarioRepository.existsById(id)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(id);

        // When
        usuarioService.eliminar(id);

        // Then
        verify(usuarioRepository, times(1)).existsById(id);
        verify(usuarioRepository, times(1)).deleteById(id);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        Long id = 99L;

        when(usuarioRepository.existsById(id)).thenReturn(false);

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.eliminar(id);
        });

        // Then
        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());

        verify(usuarioRepository, times(1)).existsById(id);
        verify(usuarioRepository, never()).deleteById(id);
    }

    @Test
    void guardar_conNombreNulo_deberiaRechazarLaSolicitud() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.guardar(new UsuarioRequest(null, "correo@duoc.cl", "12345678-9")));

        assertEquals("El nombre del usuario es obligatorio", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void guardar_conCorreoVacio_deberiaRechazarLaSolicitud() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.guardar(new UsuarioRequest("Javier", " ", "12345678-9")));

        assertEquals("El correo del usuario es obligatorio", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizar_deberiaComprobarExistenciaYConservarElId() {
        Usuario existente = new Usuario();
        existente.setId(4L);
        when(usuarioRepository.findById(4L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioService.actualizar(4L,
                new UsuarioRequest("Javiera", "javiera@duoc.cl", "11111111-1"));

        assertEquals(4L, resultado.getId());
        assertEquals("Javiera", resultado.getNombre());
        verify(usuarioRepository).findById(4L);
    }

    @Test
    void eliminar_conIdNulo_deberiaRechazarLaSolicitud() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.eliminar(null));

        assertEquals("El ID del usuario es obligatorio", exception.getMessage());
        verify(usuarioRepository, never()).existsById(any());
    }
}

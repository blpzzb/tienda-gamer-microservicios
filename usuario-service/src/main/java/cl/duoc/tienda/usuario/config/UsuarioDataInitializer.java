package cl.duoc.tienda.usuario.config;

import cl.duoc.tienda.usuario.model.Usuario;
import cl.duoc.tienda.usuario.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class UsuarioDataInitializer {

    @Bean
    CommandLineRunner cargarUsuariosDePrueba(UsuarioRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            Faker faker = new Faker(new Locale("es", "CL"));
            for (int i = 0; i < 5; i++) {
                Usuario u = new Usuario();
                u.setNombre(faker.name().fullName());
                u.setCorreo(faker.internet().emailAddress());
                u.setRut((faker.number().numberBetween(10000000, 25000000)) + "-" + faker.number().numberBetween(0, 9));
                repository.save(u);
            }
        };
    }
}

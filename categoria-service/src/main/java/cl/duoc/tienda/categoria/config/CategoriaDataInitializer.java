package cl.duoc.tienda.categoria.config;

import cl.duoc.tienda.categoria.model.Categoria;
import cl.duoc.tienda.categoria.repository.CategoriaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Locale;

/**
 * Carga datos de demostracion solo cuando la base de categorias esta vacia.
 * Esto permite probar el catalogo inmediatamente sin alterar datos existentes.
 */
@Configuration
public class CategoriaDataInitializer {

    @Bean
    CommandLineRunner cargarCategoriasDePrueba(CategoriaRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            Faker faker = new Faker(new Locale("es", "CL"));
            List<String> nombres = List.of("Consolas", "Juegos", "Accesorios", "PC Gamer", "Audio Gamer");
            repository.saveAll(nombres.stream()
                    .map(nombre -> new Categoria(nombre, faker.lorem().sentence(6)))
                    .toList());
        };
    }
}

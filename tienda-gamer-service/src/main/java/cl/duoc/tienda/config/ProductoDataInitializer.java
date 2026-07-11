package cl.duoc.tienda.config;

import cl.duoc.tienda.model.Producto;
import cl.duoc.tienda.repository.ProductoRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class ProductoDataInitializer {

    @Bean
    CommandLineRunner cargarProductosDePrueba(ProductoRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            Faker faker = new Faker(new Locale("es", "CL"));
            String[] categorias = {"Consolas", "Juegos", "Accesorios", "PC Gamer", "Audio Gamer"};
            for (int i = 0; i < 5; i++) {
                Producto p = new Producto();
                p.setNombre(faker.commerce().productName());
                p.setCategoria(categorias[faker.number().numberBetween(0, categorias.length)]);
                p.setPrecio(faker.number().randomDouble(2, 5000, 250000));
                p.setStock(faker.number().numberBetween(5, 50));
                repository.save(p);
            }
        };
    }
}

package cl.duoc.tienda.inventario.config;

import cl.duoc.tienda.inventario.model.Inventario;
import cl.duoc.tienda.inventario.repository.InventarioRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class InventarioDataInitializer {

    @Bean
    CommandLineRunner cargarInventarioDePrueba(InventarioRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            Faker faker = new Faker(new Locale("es", "CL"));
            for (long prodId = 1; prodId <= 5; prodId++) {
                Inventario inv = new Inventario();
                inv.setProductoId(prodId);
                inv.setStockActual(faker.number().numberBetween(10, 100));
                inv.setUbicacionBodega("Pasillo " + faker.number().numberBetween(1, 10) + ", Estante " + faker.number().numberBetween(1, 5));
                repository.save(inv);
            }
        };
    }
}

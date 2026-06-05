package cl.duoc.tienda.orden.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ordenServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Orden Service API")
                        .version("1.0")
                        .description("Documentación del microservicio de órdenes de la Tienda Gamer"));
    }
}
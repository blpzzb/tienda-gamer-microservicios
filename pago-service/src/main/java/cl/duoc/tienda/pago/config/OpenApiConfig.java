package cl.duoc.tienda.pago.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pagoServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pago Service API")
                        .version("1.0")
                        .description("Documentación del microservicio de pagos de la Tienda Gamer"));
    }
}
package cl.duoc.tienda.orden.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrdenConfigTest {
    @Test
    void exposesOpenApiMetadataAndWebClient() {
        assertEquals("Orden Service API", new OpenApiConfig().ordenServiceOpenAPI().getInfo().getTitle());
        assertNotNull(new WebClientConfig().webClient());
    }
}

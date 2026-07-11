package cl.duoc.tienda.pago.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PagoConfigTest {
    @Test
    void exposesOpenApiMetadataAndWebClient() {
        assertEquals("Pago Service API", new OpenApiConfig().pagoServiceOpenAPI().getInfo().getTitle());
        assertNotNull(new WebClientConfig().webClient());
    }
}

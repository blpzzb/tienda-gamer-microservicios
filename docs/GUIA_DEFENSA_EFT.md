# Guía breve de defensa EFT

## Secuencia de demostración

1. Ejecutar `docker compose up --build` y abrir Eureka en `http://localhost:8761`.
2. Mostrar los diez microservicios de negocio y el Gateway en el puerto 8090.
3. En Postman, probar `GET http://localhost:8090/productos` y crear un recurso con `POST`.
4. Enviar un cuerpo inválido y explicar el HTTP 400 generado por Bean Validation y ControllerAdvice.
5. Abrir Swagger de un servicio, por ejemplo `http://localhost:8085/swagger-ui.html`.
6. Ejecutar las pruebas Maven y abrir los reportes JaCoCo existentes.

## Flujo distribuido que debe explicarse

La creación de una orden valida usuario, producto e inventario mediante WebClient. El pago valida primero la orden. El Gateway centraliza las rutas y Eureka permite identificar servicios por su nombre lógico.

## Cambio en vivo recomendado

Agregar una validación a un DTO, crear su prueba Given-When-Then, ejecutar la prueba y demostrar en Postman que el cuerpo inválido responde 400 sin desestabilizar el servicio.

## Evidencia remota

Antes del examen, completar en el README las URLs reales de Render y guardar una colección Postman con una solicitud exitosa por el Gateway. No afirmar que Render está operativo hasta verificar la URL pública.

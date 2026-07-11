# Despliegue EFT en Render

## Lo que deja preparado el repositorio

`render.yaml` declara los 12 servicios: Eureka, los 10 servicios de negocio y el API Gateway. El Gateway recibe automáticamente las URL públicas de los demás servicios y es la única URL que se debe mostrar en la defensa.

Los servicios respetan el puerto que Render asigna mediante la variable `PORT`. No se deben fijar puertos manuales en el panel.

## Requisito externo: MySQL gestionado

Render gestiona PostgreSQL de forma nativa, pero este proyecto usa MySQL. Antes de sincronizar el Blueprint se necesita una instancia MySQL accesible por red y sus credenciales. Puede ser una instancia institucional o un proveedor MySQL administrado. No usar la base local ni publicar contraseñas en Git.

La instancia debe permitir crear o contener estos esquemas:

- `db_usuarios`
- `db_tienda_gamer`
- `db_inventario`
- `db_ordenes`
- `db_pagos`
- `db_categorias`
- `db_proveedores`
- `db_carritos`
- `db_resenas`
- `db_notificaciones`

## Despliegue

1. Subir este cambio al repositorio de GitHub y, en Render, crear un Blueprint desde ese repositorio.
2. Esperar que Render cree `tienda-gamer-eureka`. Abrir su URL pública y comprobar `GET /actuator/health`.
3. En las variables de cada servicio de negocio, definir `EUREKA_URL` con la URL de Eureka terminada en `/eureka/`. Ejemplo: `https://tienda-gamer-eureka.onrender.com/eureka/`.
4. Definir `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` para cada servicio con la cadena JDBC y las credenciales del esquema correspondiente. Ejemplo de usuarios: `DB_URL=jdbc:mysql://HOST:3306/db_usuarios?serverTimezone=UTC&useSSL=true`.
5. Ejecutar un nuevo despliegue. En la consola de Eureka deben aparecer los diez servicios registrados por nombre lógico.
6. Abrir la URL de `tienda-gamer-gateway` y comprobar `GET /actuator/health`.
7. Desde Postman, probar una ruta solo mediante Gateway, por ejemplo `GET https://URL-DEL-GATEWAY/productos` y luego `GET https://URL-DEL-GATEWAY/categorias`.

## Evidencia para la defensa

Guardar una colección Postman con estas tres peticiones y sus respuestas:

1. `GET https://URL-DEL-GATEWAY/actuator/health` devuelve `UP`.
2. `GET https://URL-DEL-GATEWAY/productos` devuelve JSON.
3. `POST https://URL-DEL-GATEWAY/ordenes` con usuario, producto y stock válidos demuestra el flujo remoto entre orden, usuario, productos e inventario.

No afirmar que el despliegue está operativo hasta haber ejecutado esas solicitudes desde una red distinta a la local.

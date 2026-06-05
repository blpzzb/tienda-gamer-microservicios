# Tienda Gamer - Arquitectura de Microservicios

## Descripción del proyecto

Este proyecto corresponde a una aplicación de **Tienda Gamer** desarrollada con arquitectura de microservicios utilizando **Spring Boot**.

El sistema permite gestionar usuarios, productos, órdenes, inventario y pagos, separando cada responsabilidad en un microservicio independiente.

Además, el proyecto incorpora comunicación REST entre microservicios mediante **WebClient**, documentación con **Swagger/OpenAPI**, pruebas unitarias con **JUnit y Mockito**, configuración mediante archivos **YAML** y un **API Gateway** para centralizar las rutas del sistema.

---

## Integrantes del equipo

* Bill Lopez
* Javier Castro
* Dante Valle
* Camilo Nelson

---

## Microservicios implementados

| Microservicio        | Puerto | Descripción                               |
| -------------------- | -----: | ----------------------------------------- |
| usuario-service      |   8081 | Gestiona los usuarios del sistema         |
| tienda-gamer-service |   8080 | Gestiona los productos gamer              |
| orden-service        |   8082 | Gestiona las órdenes de compra            |
| inventario-service   |   8083 | Gestiona el stock e inventario            |
| pago-service         |   8084 | Gestiona los pagos asociados a órdenes    |
| api-gateway          |   8090 | Centraliza el acceso a los microservicios |

---

## Arquitectura utilizada

El proyecto utiliza el patrón **CSR**, separando las responsabilidades en distintas capas:

* **Controller:** recibe las solicitudes HTTP.
* **Service:** contiene la lógica de negocio, validaciones y comunicación entre servicios.
* **Repository:** gestiona el acceso a la base de datos.
* **Model:** representa las entidades del sistema.

Cada microservicio posee su propia responsabilidad y trabaja de forma independiente.

---

## Comunicación entre microservicios

Se implementó **WebClient** para permitir la comunicación REST entre microservicios.

### Comunicación de orden-service

El microservicio `orden-service` consulta a otros servicios antes de crear una orden:

* Consulta a `usuario-service` para validar que el usuario exista.
* Consulta a `tienda-gamer-service` para validar que el producto exista.
* Consulta a `inventario-service` para validar que exista stock disponible.

### Comunicación de pago-service

El microservicio `pago-service` consulta a:

* `orden-service` para validar que la orden exista antes de registrar un pago.

---

## API Gateway

El proyecto cuenta con un **API Gateway** ejecutándose en el puerto `8090`.

Este Gateway permite centralizar el acceso a los microservicios desde una única entrada.

| Ruta Gateway                     | Microservicio destino |
| -------------------------------- | --------------------- |
| http://localhost:8090/usuarios   | usuario-service       |
| http://localhost:8090/productos  | tienda-gamer-service  |
| http://localhost:8090/ordenes    | orden-service         |
| http://localhost:8090/inventario | inventario-service    |
| http://localhost:8090/pagos      | pago-service          |

También se puede verificar el estado del Gateway con:

```http
GET http://localhost:8090/actuator/health
```

---

## Swagger / OpenAPI

Cada microservicio cuenta con documentación Swagger para visualizar y probar sus endpoints desde el navegador.

| Microservicio        | URL Swagger                           |
| -------------------- | ------------------------------------- |
| usuario-service      | http://localhost:8081/swagger-ui.html |
| tienda-gamer-service | http://localhost:8080/swagger-ui.html |
| orden-service        | http://localhost:8082/swagger-ui.html |
| inventario-service   | http://localhost:8083/swagger-ui.html |
| pago-service         | http://localhost:8084/swagger-ui.html |

---

## Bases de datos

El proyecto utiliza **MySQL** mediante **Laragon**.

Cada microservicio trabaja con su propia base de datos.

| Microservicio        | Base de datos   |
| -------------------- | --------------- |
| usuario-service      | db_usuarios     |
| tienda-gamer-service | db_tienda_gamer |
| orden-service        | db_ordenes      |
| inventario-service   | db_inventario   |
| pago-service         | db_pagos        |

---

## Configuración YAML

Todos los microservicios utilizan archivos `application.yml` para configurar:

* Nombre del servicio.
* Puerto del microservicio.
* Conexión a base de datos.
* Configuración JPA / Hibernate.
* Configuración Flyway.
* URLs de comunicación entre microservicios.
* Rutas del API Gateway.

---

## Flyway

Se utiliza **Flyway** para gestionar las migraciones de base de datos.

Cada microservicio contiene sus scripts SQL dentro de:

```text
src/main/resources/db/migration
```

Esto permite crear y versionar las tablas de cada base de datos de forma ordenada.

---

## Logs

Se implementaron logs en los servicios principales utilizando **SLF4J**.

Los logs permiten visualizar acciones importantes del sistema, por ejemplo:

* Creación de productos.
* Creación de órdenes.
* Registro de pagos.
* Consulta entre microservicios.
* Validaciones de datos.
* Errores cuando un registro no existe.
* Errores cuando un microservicio no responde.

---

## Validaciones implementadas

El proyecto incorpora validaciones de negocio en los servicios.

### Producto

* El nombre del producto es obligatorio.
* El precio debe ser mayor a 0.
* El stock no puede ser negativo.
* Se valida existencia antes de eliminar.

### Inventario

* El productoId es obligatorio.
* El stock actual no puede ser negativo.
* La ubicación de bodega es obligatoria.
* Se valida existencia antes de eliminar.

### Orden

* El usuarioId es obligatorio.
* El productoId es obligatorio.
* La cantidad debe ser mayor a 0.
* Se valida existencia de usuario, producto y stock mediante WebClient.
* Se valida existencia antes de eliminar.

### Pago

* El ordenId es obligatorio.
* El monto debe ser mayor a 0.
* Si el estado viene vacío, se asigna automáticamente como `PAGADO`.
* Se valida existencia de la orden mediante WebClient.
* Se valida existencia antes de eliminar.

---

## Pruebas unitarias

Se implementaron pruebas unitarias con **JUnit** y **Mockito** para validar la lógica de negocio de los servicios.

Pruebas creadas:

* UsuarioServiceTest
* ProductoServiceTest
* InventarioServiceTest
* OrdenServiceTest
* PagoServiceTest

Las pruebas validan:

* Listado de registros.
* Búsqueda por ID.
* Guardado correcto.
* Eliminación.
* Validaciones de negocio.
* Errores cuando no existen registros.
* Comunicación simulada con WebClient mediante Mockito.

---

## Endpoints principales

### Usuarios

```http
GET    /usuarios
GET    /usuarios/{id}
POST   /usuarios
DELETE /usuarios/{id}
```

### Productos

```http
GET    /productos
GET    /productos/{id}
POST   /productos
DELETE /productos/{id}
```

### Órdenes

```http
GET    /ordenes
GET    /ordenes/{id}
POST   /ordenes
DELETE /ordenes/{id}
```

### Inventario

```http
GET    /inventario
GET    /inventario/{id}
GET    /inventario/producto/{productoId}
POST   /inventario
DELETE /inventario/{id}
```

### Pagos

```http
GET    /pagos
GET    /pagos/{id}
POST   /pagos
DELETE /pagos/{id}
```

---

## Ejecución del proyecto

Antes de iniciar los microservicios, se debe iniciar **Laragon/MySQL**.

Orden recomendado de ejecución:

1. usuario-service
2. tienda-gamer-service
3. inventario-service
4. orden-service
5. pago-service
6. api-gateway

---

## Pruebas mediante API Gateway

Con todos los servicios activos, se pueden probar las rutas desde el Gateway:

```http
GET http://localhost:8090/usuarios
GET http://localhost:8090/productos
GET http://localhost:8090/ordenes
GET http://localhost:8090/inventario
GET http://localhost:8090/pagos
```

---

## Ejemplo de creación de usuario

```http
POST http://localhost:8090/usuarios
```

```json
{
  "nombre": "Javier",
  "correo": "javier@gmail.com",
  "rut": "12345678-9"
}
```

---

## Ejemplo de creación de producto

```http
POST http://localhost:8090/productos
```

```json
{
  "nombre": "Mouse Gamer",
  "categoria": "Perifericos",
  "precio": 25000,
  "stock": 10
}
```

---

## Ejemplo de creación de inventario

```http
POST http://localhost:8090/inventario
```

```json
{
  "productoId": 1,
  "stockActual": 10,
  "ubicacionBodega": "Bodega Central"
}
```

---

## Ejemplo de creación de orden

```http
POST http://localhost:8090/ordenes
```

```json
{
  "usuarioId": 1,
  "productoId": 1,
  "cantidad": 2
}
```

Antes de crear la orden, el sistema valida mediante WebClient que:

* El usuario exista.
* El producto exista.
* Exista stock suficiente.

---

## Ejemplo de creación de pago

```http
POST http://localhost:8090/pagos
```

```json
{
  "ordenId": 1,
  "monto": 50000,
  "estado": "PAGADO"
}
```

Antes de registrar el pago, el sistema valida mediante WebClient que la orden exista.

---

## Herramientas utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Cloud Gateway
* WebClient
* MySQL
* Laragon
* Flyway
* Swagger / OpenAPI
* JUnit
* Mockito
* Maven
* IntelliJ IDEA
* Postman
* GitHub
* Trello

---

## Resumen técnico

El sistema está compuesto por cinco microservicios principales y un API Gateway.

Cada microservicio posee su propia estructura, base de datos y configuración.
La comunicación entre servicios se realiza mediante WebClient, permitiendo validar información entre microservicios antes de guardar datos importantes como órdenes y pagos.

El API Gateway permite centralizar el consumo de las APIs desde el puerto `8090`.
Swagger permite documentar y probar los endpoints de cada servicio.
Las pruebas unitarias validan la lógica principal del sistema utilizando JUnit y Mockito.

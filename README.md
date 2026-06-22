# Proyecto FullStack Prueba 3 - Tienda Gamer

## Descripción del proyecto

Este proyecto corresponde a una arquitectura de microservicios desarrollada con **Spring Boot**, orientada a la gestión de una tienda gamer.

El sistema permite administrar usuarios, productos, inventario, órdenes y pagos, utilizando una arquitectura distribuida donde cada funcionalidad principal se encuentra separada en un microservicio independiente.

La comunicación entre servicios se realiza mediante peticiones REST y el acceso centralizado se gestiona por medio de un **API Gateway**.

---

## Arquitectura del sistema

El proyecto está compuesto por los siguientes servicios:

| Servicio             | Descripción                                                          | Puerto |
| -------------------- | -------------------------------------------------------------------- | -----: |
| api-gateway          | Servicio encargado de centralizar las rutas hacia los microservicios |   8090 |
| usuario-service      | Microservicio encargado de la gestión de usuarios                    |   8081 |
| tienda-gamer-service | Microservicio encargado de la gestión de productos                   |   8080 |
| inventario-service   | Microservicio encargado de la gestión de inventario                  |   8083 |
| orden-service        | Microservicio encargado de la gestión de órdenes                     |   8082 |
| pago-service         | Microservicio encargado de la gestión de pagos                       |   8084 |
| mysql-fullstack      | Base de datos MySQL utilizada por los microservicios                 |   3307 |

---

## Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* MySQL
* Flyway
* Maven
* Docker
* Docker Compose
* API Gateway
* WebClient
* Swagger / OpenAPI
* JUnit
* Mockito

---

## Estructura general del proyecto

```text
FullStack Prueba 3
├── api-gateway
├── usuario-service
├── tienda-gamer-service
├── inventario-service
├── orden-service
├── pago-service
├── docker-compose.yml
├── README.md
└── pom.xml
```

Cada microservicio mantiene una estructura basada en el patrón:

```text
Controller - Service - Repository - Model
```

Esta separación permite organizar el código por responsabilidades, manteniendo una arquitectura más limpia y fácil de mantener.

---

## Microservicios implementados

### 1. api-gateway

Servicio encargado de centralizar las peticiones hacia los microservicios.

Puerto:

```text
8090
```

Rutas configuradas:

```text
/usuarios/**
/productos/**
/inventario/**
/ordenes/**
/pagos/**
```

---

### 2. usuario-service

Servicio encargado de la gestión de usuarios.

Puerto:

```text
8081
```

Base de datos:

```text
db_usuarios
```

---

### 3. tienda-gamer-service

Servicio encargado de la gestión de productos de la tienda gamer.

Puerto:

```text
8080
```

Base de datos:

```text
db_tienda_gamer
```

---

### 4. inventario-service

Servicio encargado de la gestión del inventario de productos.

Puerto:

```text
8083
```

Base de datos:

```text
db_inventario
```

---

### 5. orden-service

Servicio encargado de la gestión de órdenes.

Puerto:

```text
8082
```

Base de datos:

```text
db_ordenes
```

Este microservicio se comunica con:

```text
usuario-service
tienda-gamer-service
inventario-service
```

---

### 6. pago-service

Servicio encargado de la gestión de pagos.

Puerto:

```text
8084
```

Base de datos:

```text
db_pagos
```

Este microservicio se comunica con:

```text
orden-service
```

---

## Configuración de Docker

Cada microservicio cuenta con un archivo:

```text
Dockerfile
```

Este archivo permite construir una imagen Docker individual para cada servicio.

Además, en la carpeta principal del proyecto existe el archivo:

```text
docker-compose.yml
```

Este archivo permite levantar todos los servicios al mismo tiempo, incluyendo:

```text
api-gateway
usuario-service
tienda-gamer-service
inventario-service
orden-service
pago-service
mysql-fullstack
```

---

## Ejecución del proyecto con Docker

### Requisitos previos

Antes de ejecutar el proyecto se debe tener instalado:

```text
Docker Desktop
Docker Compose
```

También se recomienda tener instalado:

```text
Java 21
Maven
```

---

### Levantar todos los servicios

Desde la carpeta principal del proyecto, donde se encuentra el archivo `docker-compose.yml`, ejecutar:

```bash
docker compose up --build -d
```

Este comando construye las imágenes de los microservicios y levanta todos los contenedores en segundo plano.

---

### Verificar contenedores activos

Para comprobar que los servicios se están ejecutando correctamente:

```bash
docker compose ps
```

Los servicios deben aparecer en estado:

```text
Up
```

El contenedor de MySQL debe aparecer como:

```text
healthy
```

---

### Detener los servicios

Para detener los contenedores:

```bash
docker compose down
```

---

### Detener y eliminar datos persistentes

Si se desea detener los servicios y eliminar el volumen de datos de MySQL:

```bash
docker compose down -v
```

---

## Puertos utilizados

| Servicio             | Puerto local | Puerto contenedor |
| -------------------- | -----------: | ----------------: |
| api-gateway          |         8090 |              8090 |
| usuario-service      |         8081 |              8081 |
| tienda-gamer-service |         8080 |              8080 |
| inventario-service   |         8083 |              8083 |
| orden-service        |         8082 |              8082 |
| pago-service         |         8084 |              8084 |
| mysql-fullstack      |         3307 |              3306 |

MySQL se expone en el puerto local:

```text
3307
```

Esto evita conflictos con instalaciones locales como Laragon, que normalmente utilizan el puerto:

```text
3306
```

---

## Verificación de funcionamiento

### Verificar API Gateway

Abrir en el navegador:

```text
http://localhost:8090/actuator/health
```

Respuesta esperada:

```json
{
  "status": "UP"
}
```

También puede mostrarse una respuesta similar a:

```json
{
  "groups": [
    "liveness",
    "readiness"
  ],
  "status": "UP"
}
```

Esto indica que el API Gateway está funcionando correctamente.

---

### Verificar microservicios directamente

Usuario Service:

```text
http://localhost:8081/actuator/health
```

Tienda Gamer Service:

```text
http://localhost:8080/actuator/health
```

Inventario Service:

```text
http://localhost:8083/actuator/health
```

Orden Service:

```text
http://localhost:8082/actuator/health
```

Pago Service:

```text
http://localhost:8084/actuator/health
```

La respuesta esperada para cada servicio es:

```json
{
  "status": "UP"
}
```

---

## Rutas principales mediante API Gateway

El API Gateway utiliza el puerto:

```text
8090
```

Las rutas principales son:

```text
http://localhost:8090/usuarios
http://localhost:8090/productos
http://localhost:8090/inventario
http://localhost:8090/ordenes
http://localhost:8090/pagos
```

Estas rutas redirigen hacia los microservicios correspondientes.

---

## Comunicación entre microservicios

El sistema utiliza comunicación REST entre microservicios.

El microservicio `orden-service` consume información desde:

```text
usuario-service
tienda-gamer-service
inventario-service
```

El microservicio `pago-service` consume información desde:

```text
orden-service
```

Para permitir que la comunicación funcione tanto de manera local como dentro de Docker, se utilizan variables de entorno en los archivos `application.yml`.

Ejemplo:

```yaml
services:
  orden:
    url: ${ORDEN_SERVICE_URL:http://localhost:8082}
```

Cuando el sistema se ejecuta localmente, se utiliza `localhost`.

Cuando el sistema se ejecuta con Docker, Docker Compose reemplaza la URL por el nombre del contenedor, por ejemplo:

```text
http://orden-service:8082
```

---

## Bases de datos utilizadas

Cada microservicio utiliza su propia base de datos, manteniendo separación de responsabilidades.

| Microservicio        | Base de datos   |
| -------------------- | --------------- |
| usuario-service      | db_usuarios     |
| tienda-gamer-service | db_tienda_gamer |
| inventario-service   | db_inventario   |
| orden-service        | db_ordenes      |
| pago-service         | db_pagos        |

Todas las bases de datos se ejecutan sobre el contenedor:

```text
mysql-fullstack
```

---

## Configuración YAML

Los microservicios utilizan archivos:

```text
application.yml
```

Estos archivos permiten configurar:

```text
nombre del servicio
puerto de ejecución
conexión a base de datos
configuración JPA
configuración Flyway
rutas de comunicación entre microservicios
actuator health
```

El uso de variables de entorno permite que el proyecto funcione tanto en ejecución local como en ejecución con Docker.

---

## Flyway

El proyecto utiliza Flyway para ejecutar migraciones de base de datos.

Las migraciones se encuentran en:

```text
src/main/resources/db/migration
```

Esto permite versionar y controlar la creación de tablas y datos iniciales dentro de cada microservicio.

---

## Swagger / OpenAPI

El proyecto considera documentación técnica mediante Swagger/OpenAPI para facilitar la revisión y prueba de endpoints.

Las rutas de Swagger normalmente se pueden revisar en cada microservicio usando:

```text
http://localhost:8081/swagger-ui/index.html
http://localhost:8080/swagger-ui/index.html
http://localhost:8083/swagger-ui/index.html
http://localhost:8082/swagger-ui/index.html
http://localhost:8084/swagger-ui/index.html
```

Estas rutas permiten visualizar los endpoints disponibles, parámetros, respuestas y modelos utilizados por cada servicio.

---

## Pruebas unitarias

El proyecto considera pruebas unitarias utilizando:

```text
JUnit
Mockito
```

Las pruebas se encuentran en:

```text
src/test/java
```

Estas pruebas permiten validar la lógica de negocio de los servicios y comprobar que el comportamiento esperado se cumple correctamente.

---

## Comandos útiles

Construir y levantar todos los servicios:

```bash
docker compose up --build -d
```

Ver estado de los contenedores:

```bash
docker compose ps
```

Ver logs de un servicio específico:

```bash
docker logs api-gateway --tail=100
```

Ejemplo para revisar logs de `orden-service`:

```bash
docker logs orden-service --tail=100
```

Detener servicios:

```bash
docker compose down
```

Detener servicios y eliminar datos de MySQL:

```bash
docker compose down -v
```

---

## Evidencias de ejecución

Para la defensa técnica se puede mostrar:

```text
Docker Desktop con los contenedores activos
PowerShell ejecutando docker compose ps
API Gateway funcionando en http://localhost:8090/actuator/health
Microservicios respondiendo con status UP
MySQL en estado healthy
Endpoints funcionando desde el API Gateway
```

---

## Explicación técnica resumida

Este proyecto aplica una arquitectura de microservicios donde cada servicio tiene una responsabilidad específica.
El API Gateway centraliza las solicitudes y redirige hacia el microservicio correspondiente.

Cada microservicio cuenta con su propia configuración, puerto, base de datos y estructura interna basada en capas.
La comunicación entre servicios se realiza mediante REST, permitiendo que los servicios trabajen de forma independiente pero conectada.

Docker permite ejecutar todo el ecosistema de manera ordenada, levantando la base de datos, los microservicios y el API Gateway con un solo comando.

---

## Autores

Proyecto desarrollado para la asignatura:

```text
Desarrollo FullStack 1
```

Evaluación:

```text
Evaluación Parcial 3
```

Integrantes:

```text
- Javier Castro Farias
- Integrante 2
- Integrante 3
- Integrante 4
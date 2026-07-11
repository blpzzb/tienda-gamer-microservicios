FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn -pl inventario-service -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/inventario-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

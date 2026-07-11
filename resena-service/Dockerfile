FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn -pl resena-service -am -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/resena-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

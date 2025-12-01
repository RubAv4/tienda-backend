# ===== ETAPA 1: COMPILAR EL PROYECTO =====
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom y descargamos dependencias en cache
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Copiamos el código fuente y compilamos
COPY src ./src
RUN mvn -q -DskipTests package

# ===== ETAPA 2: IMAGEN LIGERA DE EJECUCIÓN =====
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# ⚠️ Ajusta el nombre del JAR si es distinto
COPY --from=build /app/target/tienda-electronica-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

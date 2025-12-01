# ===== ETAPA 1: COMPILAR EL PROYECTO (JAVA 21) =====
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos pom y descargamos dependencias en caché
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Copiamos el código fuente y compilamos
COPY src ./src
RUN mvn -q -DskipTests package

# ===== ETAPA 2: IMAGEN LIGERA DE EJECUCIÓN (JAVA 21) =====
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# ⚠️ Asegúrate de que el nombre del JAR coincide con el que genera Maven
COPY --from=build /app/target/tienda-electronica-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

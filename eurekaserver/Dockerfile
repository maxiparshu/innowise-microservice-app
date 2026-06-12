FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
COPY eurekaserver ./eurekaserver

RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

RUN ./gradlew :eurekaserver:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/eurekaserver/build/libs/*.jar app.jar

EXPOSE 8800

ENTRYPOINT ["java", "-jar", "app.jar"]
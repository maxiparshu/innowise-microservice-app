FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
COPY gateway ./gateway

RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

RUN ./gradlew :gateway:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/gateway/build/libs/*.jar app.jar

EXPOSE 8066

ENTRYPOINT ["java", "-jar", "app.jar"]
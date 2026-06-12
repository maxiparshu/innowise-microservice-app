FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
COPY orderservice ./orderservice

RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

RUN ./gradlew :orderservice:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/orderservice/build/libs/*.jar app.jar

EXPOSE 8003

ENTRYPOINT ["java", "-jar", "app.jar"]
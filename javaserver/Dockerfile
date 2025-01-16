FROM gradle:8.1-jdk17 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts /app/

RUN gradle dependencies --no-daemon || true

COPY src /app/src

RUN gradle build --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]
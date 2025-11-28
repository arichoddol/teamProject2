
FROM eclipse-temurin:17-jdk AS builder

# 작업 디렉토리 설정
WORKDIR /app

COPY gradlew build.gradle settings.gradle ./
RUN chmod +x gradlew
COPY backendspring/gradle ./gradle
RUN ./gradlew dependencies
COPY backendspring/src ./src
RUN ./gradlew bootJar



FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar


EXPOSE 8090

CMD ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]

FROM eclipse-temurin:17-jdk AS builder

# 작업 디렉토리 설정
WORKDIR /app

COPY /build/libs/*.jar app.jar

FROM eclipse-temurin:17-jre


# 필요한 종속성 미리 다운로드 (캐싱 활용)
RUN ./gradlew dependencies

EXPOSE 8090

CMD ["java", "-jar", "/app/app.jar"]
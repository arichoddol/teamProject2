# ========== Stage 1: 빌더 (Build Stage) ==========
# Gradle을 사용해 빌드하기 위한 JDK 이미지
FROM openjdk:17-jdk-slim AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 및 설정 파일을 복사하고 권한 부여
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# 필요한 종속성 미리 다운로드 (캐싱 활용)
RUN ./gradlew dependencies

# 소스 코드 복사
COPY src ./src

# 프로젝트 빌드
# JAR 파일은 /app/build/libs/ 에 생성됩니다.
RUN ./gradlew bootJar

# ========== Stage 2: 런타임 (Runtime Stage) ==========
# 실행만을 위한 경량 JRE 이미지 사용 (최적화)
FROM openjdk:17-jre

# 컨테이너의 작업 디렉토리를 /app으로 설정
WORKDIR /app

# 빌더 스테이지에서 생성된 JAR 파일을 복사
# 파일 이름이 app.jar이라고 가정합니다.
# 실제 파일 경로: /app/build/libs/*.jar
COPY --from=builder /app/build/libs/*.jar app.jar

# Spring Boot 애플리케이션의 포트 노출
EXPOSE 8090

# 컨테이너 실행 명령어
CMD ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]

#TEST CICD
#TESTTT
# Stage 1: 빌드
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Gradle 래퍼 + 설정 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 소스 복사
COPY src src

# WAR 빌드 (다운로드 캐시 활용)
RUN chmod +x gradlew && ./gradlew war --no-daemon

# Stage 2: 실행 (ARM/amd64 공통)
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# 빌드된 WAR만 복사
COPY --from=builder /app/build/libs/bible-*.war app.war

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]

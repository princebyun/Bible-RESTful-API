# Stage 1: 빌드
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# 실행 가능 JAR 생성 (bootJar - Gradle 8.x에서 bootWar 미등록 이슈 회피)
RUN chmod +x gradlew && ./gradlew bootJar -x test --no-daemon

# Stage 2: 실행
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# bootJar로 생성된 실행 가능 JAR 복사
COPY --from=builder /app/build/libs/bible-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
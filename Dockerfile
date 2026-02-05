# Stage 1: 빌드
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# 실행 가능한 WAR 생성 (bootWar 사용, plain.war 제외)
RUN chmod +x gradlew && ./gradlew bootWar -x test --no-daemon

# Stage 2: 실행
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# bootWar로 생성된 실행 가능 WAR 복사 (build.gradle 기준: group=bible, version=0.0.1-SNAPSHOT)
COPY --from=builder /app/build/libs/bible-0.0.1-SNAPSHOT.war app.war

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
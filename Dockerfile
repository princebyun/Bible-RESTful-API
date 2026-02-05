# Stage 1: 빌드
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# plain.war 생성을 막기 위해 bootWar 명령어를 명시적으로 사용
RUN chmod +x gradlew && ./gradlew  builder -x test --no-daemon

# Stage 2: 실행
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 🔴 핵심 수정: *.war 대신 정확한 파일명을 콕 집어서 복사합니다.
# (plain.war가 있어도 무시하고 실행 가능한 놈만 가져옵니다)
COPY --from=builder /app/build/libs/bible-0.0.1-SNAPSHOT-plani.war app.war

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
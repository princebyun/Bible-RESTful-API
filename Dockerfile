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

# WAR 빌드 (테스트 제외하고 빠르게 빌드)
# 'id war' 플러그인 때문에 결과물은 build/libs/프로젝트명-버전.war로 생성됩니다.
RUN chmod +x gradlew && ./gradlew clean build -x test --no-daemon

# Stage 2: 실행 (ARM/amd64 공통)
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# 🔴 변경된 부분:
# 1. 원본 파일이 .war이므로 *.war를 찾습니다. (이름이 달라도 찾을 수 있게 와일드카드 사용)
# 2. 복사할 이름을 app.war로 변경합니다.
COPY --from=builder /app/build/libs/*.war app.war

EXPOSE 8080

# 🔴 변경된 부분:
# Spring Boot로 만든 WAR는 JAR처럼 바로 실행 가능합니다. app.war를 실행합니다.
ENTRYPOINT ["java", "-jar", "app.war"]
# ARM(linux/arm64) 및 amd64(linux/amd64) 호환 - Spring Boot 내장 Tomcat
# ARM 서버에서 docker build 시 자동으로 linux/arm64 이미지 사용 (수정 불필요)
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# ARM/AMD 공통: 빌드된 WAR 복사 (호스트에서 ./gradlew war 후)
COPY build/libs/bible-*.war app.war

# 내장 톰캣으로 실행 (포트 8080)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]

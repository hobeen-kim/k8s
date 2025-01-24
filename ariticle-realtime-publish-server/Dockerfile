# 빌드 스테이지
FROM gradle:jdk21 AS builder

WORKDIR /app

# 의존성 다운로드를 위한 설정 파일만 복사
COPY build.gradle.kts settings.gradle.kts /app/
# 의존성 다운로드
RUN gradle dependencies --no-daemon

# 소스 코드 복사 및 빌드
COPY src /app/src
RUN gradle clean build --no-daemon

# 실행 스테이지
FROM openjdk:21-jdk-slim
WORKDIR /app

# 빌드 스테이지에서 생성된 jar 파일만 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# JVM 설정
ENV JAVA_OPTS=""

# 컨테이너 실행 시 수행할 명령
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
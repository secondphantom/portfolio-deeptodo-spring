# 1단계: Gradle을 사용하여 빌드
FROM gradle:8.8.0-jdk17 AS build

# 프로젝트 루트 디렉토리에서 소스 코드와 Gradle 설정 파일들을 복사
COPY . /app

# 작업 디렉토리 설정
WORKDIR /app

# Gradle을 사용하여 애플리케이션 빌드
RUN gradle clean build

# 2단계: 빌드된 애플리케이션을 JDK로 실행
FROM openjdk:17-jdk-slim

# 빌드된 JAR 파일을 복사
COPY --from=build /app/build/libs/app.jar /app/app.jar

# 작업 디렉토리 설정
WORKDIR /app

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

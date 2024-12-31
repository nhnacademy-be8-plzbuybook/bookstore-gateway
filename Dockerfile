# 1. 사용할 베이스 이미지 (Temurin)
FROM eclipse-temurin:21-jre
# 2. 작업 디렉토리 설정
WORKDIR /app
# 3. JAR 파일 복사
COPY target/bookstore-gateway-0.0.1-SNAPSHOT.jar /app/gateway.jar
# 4. 게이트웨이 컨테이너는 8080 포트를 기본적으로 사용함.
EXPOSE 8080
# 4. 컨테이너 시작 시 실행할 명령어
CMD ["java", "-jar", "gateway.jar"]
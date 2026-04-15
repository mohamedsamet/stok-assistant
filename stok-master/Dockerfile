FROM eclipse-temurin:17-jre-alpine

ARG JAR_FILE=target/*.jar

COPY ./target/stok-*.jar app.jar

COPY src/main/resources .

EXPOSE 8008

ENTRYPOINT ["java", "-jar", "app.jar"]
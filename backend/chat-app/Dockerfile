FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY . .
RUN mvn package -DskipTests -f backend/chat-app/pom.xml

FROM eclipse-temurin:25
COPY --from=build /app/backend/chat-app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
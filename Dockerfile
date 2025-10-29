FROM maven:3-eclipse-temurin-21-alpine AS builder
COPY pom.xml ./
COPY mvnw ./
COPY .mvn/ .mvn/
COPY src/ src/
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=builder target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# Build stage
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /build
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src
RUN ./mvnw package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/target/ExpensesTracker-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]


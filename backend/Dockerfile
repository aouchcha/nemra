# syntax=docker/dockerfile:1.7

FROM maven:3.9.9-eclipse-temurin-21 AS deps
WORKDIR /workspace

COPY pom.xml ./
COPY .mvn .mvn
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -DskipTests dependency:go-offline

FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY --from=deps /root/.m2 /root/.m2
COPY pom.xml ./
COPY src src

RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests package

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY --from=build /workspace/target/nemra-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

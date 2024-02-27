FROM gradle:jdk17 AS build
WORKDIR /workspace
COPY . .
RUN gradle bootJar --no-daemon

FROM openjdk:17-alpine
EXPOSE 8080
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
FROM openjdk:8-jdk-slim as build
WORKDIR /app
COPY .  /app
RUN ./gradlew clean build

FROM openjdk:8-jre-slim
WORKDIR /app
COPY --from=build /app/build/libs/shipping-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-jar", "shipping-0.0.1-SNAPSHOT.jar"]
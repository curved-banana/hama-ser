FROM openjdk:11

WORKDIR /app

COPY build/libs/hamahama-0.0.1-SNAPSHOT.jar /app/app.jar
COPY out/production/resources/application.yml /app/application.yml
COPY out/production/resources/templates /app/templates

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

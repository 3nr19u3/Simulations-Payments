FROM eclipse-temurin:17

LABEL developer="kike123kob@gmail.com"

WORKDIR /app

COPY ../balance-service/target/balance-service-0.0.1-SNAPSHOT.jar /app/balance-service.jar

ENTRYPOINT["java","-jar","balance-service.jar"]


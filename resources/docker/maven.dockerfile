FROM openjdk:17
ARG JAR_FILE=target/*-SNAPSHOT.jar
WORKDIR /app
ENV APP_ENV=prod
RUN mkdir -p /images
RUN chmod 777 /images
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT java -jar -Dspring.profiles.active=$APP_ENV app.jar
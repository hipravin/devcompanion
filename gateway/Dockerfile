FROM openjdk:17-alpine
ARG user=spring
ARG group=spring
ARG JAR_FILE=target/*.jar

RUN addgroup -S ${group} && adduser -S ${user} -G ${group}

USER ${user}

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=docker"]
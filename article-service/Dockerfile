FROM openjdk:17-alpine
ARG user=spring
ARG group=spring
ARG JAR_FILE=target/*.jar

RUN addgroup -S ${group} && adduser -S ${user} -G ${group}

# article-repo - not decided how to handle
#VOLUME /usr/local/articles-repo
#USER ${user}

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=docker,initrunners"]
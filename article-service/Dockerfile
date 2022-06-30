FROM openjdk:17-alpine
ARG user=spring
ARG group=spring
ARG JAR_FILE=target/*.jar

RUN addgroup -S ${group} && adduser -S ${user} -G ${group}

# article-repo - not decided how to handle
#VOLUME /usr/local/article-repo
#USER ${user}

#RUN mkdir -p /usr/local/article-repo

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=docker,initrunners"]
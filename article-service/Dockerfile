FROM openjdk:17-alpine
#FROM eclipse-temurin:17-jdk-jammy

ARG JAR_FILE=target/*.jar

#LABEL project.name=${project.artifactId} \
#      project.version=${project.version}

LABEL custom.label="article-service-docker-image-custom-label"

COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-jar","/app.jar","--spring.profiles.active=initrunners"]
#ENTRYPOINT ["java", "-jar","-XX:+FlightRecorder","-XX:StartFlightRecording=settings=profile,delay=5s,disk=true,dumponexit=true,duration=200s,filename=myrecording.jfr","/app.jar","--spring.profiles.active=initrunners"]
ENTRYPOINT ["java", "-jar","-XX:+FlightRecorder","-XX:StartFlightRecording=settings=profile,disk=true,dumponexit=true,maxage=4h,maxsize=400MB,filename=myrecording.jfr","/app.jar","--spring.profiles.active=initrunners"]

#ENTRYPOINT exec java -Xmx1512m -Xms1g [...] -cp app:app/lib/* com.schibsted.yada.Yada
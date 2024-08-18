# FROM openjdk:17-jdk-alpine
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY target/eeg4asd-backend-0.1.0-SNAPSHOT.jar eeg4asd.jar
EXPOSE 8080
# ENTRYPOINT exec java $JAVA_OPTS -jar eeg4asd.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar eeg4asd.jar

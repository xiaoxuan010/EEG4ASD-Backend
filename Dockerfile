# FROM openjdk:17-jdk-alpine
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY target/eeg4asd-backend.jar /opt/eeg4asd/eeg4asd-backend.jar
EXPOSE 8080
# ENTRYPOINT exec java $JAVA_OPTS -jar eeg4asd.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /opt/eeg4asd/eeg4asd-backend.jar

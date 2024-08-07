FROM openjdk:8u171-jdk-alpine
VOLUME /tmp
ADD /build/libs/spielplan-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

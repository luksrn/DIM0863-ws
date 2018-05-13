FROM frolvlad/alpine-oraclejdk8
ARG JAR_FILE
ENV SPRING_PROFILES_ACTIVE docker
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

EXPOSE 8080

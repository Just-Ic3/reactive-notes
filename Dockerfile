FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/notes.jar app.jar
ADD docker.properties application.properties
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5050"
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar
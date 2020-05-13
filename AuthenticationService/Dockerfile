FROM java:8-jre
WORKDIR usr/src
ENV MYSQL_HOST localhost
ENV MYSQL_PASSWORD root
ENV MYSQL_USERNAME root
ADD ./target/AuthenticationService-0.0.1-SNAPSHOT.jar /usr/src/AuthenticationService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","AuthenticationService-0.0.1-SNAPSHOT.jar"]
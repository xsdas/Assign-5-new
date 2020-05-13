FROM java:8-jre
WORKDIR usr/src
ENV MONGO_HOST localhost
ENV MONGO_PORT 27017
ADD ./target/NewsSourceService-0.0.1-SNAPSHOT.jar /usr/src/NewsSourceService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","NewsSourceService-0.0.1-SNAPSHOT.jar"]
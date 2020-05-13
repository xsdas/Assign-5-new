FROM java:8-jre
WORKDIR usr/src
ENV MONGO_HOST localhost
ENV MONGO_PORT 27017
ADD ./target/UserProfileService-0.0.1-SNAPSHOT.jar /usr/src/UserProfileService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","UserProfileService-0.0.1-SNAPSHOT.jar"]
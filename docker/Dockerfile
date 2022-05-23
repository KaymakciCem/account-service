FROM openjdk

VOLUME /tmp

ADD maven/account-service-0.0.1-SNAPSHOT.jar account-service.jar

ENTRYPOINT ["java","-jar","account-service.jar"]
FROM openjdk:17-oracle
COPY target/library-0.0.1-SNAPSHOT.jar library.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","library.jar"]

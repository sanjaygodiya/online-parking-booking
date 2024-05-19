FROM openjdk:17.0.2
VOLUME /tmp
EXPOSE 8080
ADD target/parkingmanagementsystem-0.0.1-SNAPSHOT.jar parkingmanagementsystem-0.0.1-SNAPSHOT.jar 
ENTRYPOINT ["java","-jar","/parkingmanagementsystem-0.0.1-SNAPSHOT.jar"]
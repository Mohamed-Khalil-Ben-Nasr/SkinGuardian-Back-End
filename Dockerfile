FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY target/skinguardian-backend-0.0.1-SNAPSHOT.jar /opt/app
EXPOSE 8085
CMD ["java", "-jar", "/opt/app/skinguardian-backend-0.0.1-SNAPSHOT.jar"]
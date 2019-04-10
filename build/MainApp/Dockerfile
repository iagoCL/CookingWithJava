FROM openjdk:8-jdk-alpine
MAINTAINER TheJavaCookers 
ENV USED_PORT 8080
RUN mkdir /opt/application
COPY ./CookingWithJava.jar /opt/application/CookingWithJava.jar
CMD java -jar /opt/application/CookingWithJava.jar -d -puertoWeb$USED_PORT
FROM openjdk:8-jdk-alpine
MAINTAINER TheJavaCookers 
ENV USED_PORT 7001
RUN mkdir /opt/application
COPY ./CookingWithJavaInternalService.jar /opt/application/CookingWithJavaInternalService.jar
CMD java -jar /opt/application/CookingWithJavaInternalService.jar -d
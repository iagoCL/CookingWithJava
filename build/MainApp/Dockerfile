FROM openjdk:8-jdk-alpine
MAINTAINER TheJavaCookers 
EXPOSE 8443
RUN mkdir /opt/application
COPY ./CookingWithJava.jar /opt/application/CookingWithJava.jar
CMD ["java","-jar","/opt/application/CookingWithJava.jar"]
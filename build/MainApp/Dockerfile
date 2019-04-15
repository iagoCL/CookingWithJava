FROM openjdk:8-jdk-alpine
MAINTAINER TheJavaCookers 
ENV INTERNAL_SERVICE_URL http://InternalServiceProxy:7000
ENV HAZELCAST_URL1 MainApp1
ENV HAZELCAST_URL2 MainApp2
RUN mkdir /opt/application
COPY ./CookingWithJava.jar /opt/application/CookingWithJava.jar
COPY ./wait /wait
RUN chmod +x /wait
CMD /wait && java -jar /opt/application/CookingWithJava.jar -d -urlServicioInterno$INTERNAL_SERVICE_URL -urlHazelCast$HAZELCAST_URL1 -urlHazelCast$HAZELCAST_URL2
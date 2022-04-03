FROM openjdk:11
MAINTAINER CELAL_KARTAL
EXPOSE 8081
ADD target/grocery-delivery-service.jar grocery-delivery-service.jar
ENTRYPOINT ["java","-jar","/grocery-delivery-service.jar"]


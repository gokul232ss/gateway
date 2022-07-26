FROM openjdk:11
EXPOSE 8080
ADD target/gateway_local.jar gateway_local.jar
ENTRYPOINT ["java", "-jar", "gateway_local.jar"]
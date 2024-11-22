FROM amazoncorretto:21
WORKDIR /app
COPY ./build/libs/secret-manager-1.0.0.jar /app/secret-manager.jar

ENV TZ=Asia/Seoul

CMD ["java", "-jar", "secret-manager.jar"]

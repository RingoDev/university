# Stage 1
FROM openjdk:15

COPY ./out/artifacts/accounting_jar/accounting.jar /usr/app/

WORKDIR /usr/app

RUN mkdir fibu

EXPOSE 1099

ENTRYPOINT ["java", "-jar", "accounting.jar"]

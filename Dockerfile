
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk17-temurin

RUN sed -i 's/port="8005"/port="-1"/' /usr/local/tomcat/conf/server.xml

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/expense-tracker.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

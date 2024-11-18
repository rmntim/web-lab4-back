FROM maven:3.8.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY ./pom.xml ./pom.xml
COPY ./src ./src

RUN mvn clean package

FROM quay.io/wildfly/wildfly:34.0.0.Final-jdk21

RUN wget https://jdbc.postgresql.org/download/postgresql-42.3.1.jar -O /tmp/postgresql.jar \
    && /opt/jboss/wildfly/bin/jboss-cli.sh --command="module add --name=org.postgresql --resources=/tmp/postgresql.jar --dependencies=javax.api,javax.transaction.api"

COPY --from=build /app/target/ROOT.war /opt/jboss/wildfly/standalone/deployments/ROOT.war

COPY ./standalone.xml /opt/jboss/wildfly/standalone/configuration/

EXPOSE 8080

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-D", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

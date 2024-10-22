FROM gradle:8.8-jdk21 AS build

WORKDIR /app

COPY ./build.gradle ./
COPY ./settings.gradle ./
COPY ./gradle ./gradle
COPY ./src ./src

RUN gradle build

FROM quay.io/wildfly/wildfly:34.0.0.Final-jdk21 AS runtime

# Replace credentials with your own
RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

COPY --from=build /app/build/libs/*.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080 9990

RUN sed -i 's;<spec-descriptor-property-replacement>false</spec-descriptor-property-replacement>;<spec-descriptor-property-replacement>true</spec-descriptor-property-replacement>;' /opt/jboss/wildfly/standalone/configuration/standalone.xml

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
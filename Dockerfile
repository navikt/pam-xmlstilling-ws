FROM navikt/java:12
COPY target/pam-xmlstilling-ws-*.jar app.jar
EXPOSE 9022

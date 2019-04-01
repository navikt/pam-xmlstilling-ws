FROM navikt/java:8
COPY target/pam-xmlstilling-ws-*.jar app.jar
EXPOSE 9022

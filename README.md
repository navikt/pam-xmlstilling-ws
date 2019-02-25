# xmlstilling-ws
SOAP-basert WebService for å motta stillinger på hr-xml-format fra eksterne aktører 

### Kjøre DevApplication lokalt
Kopier application-dev.yaml til USER_HOME og gi den nytt navn xmlstilling-ws-dev.yaml.
Slå opp database-properties i fasit og oppdater filen med dette.

Velg "Edit configurations" for DevApplication
Legg til i "Program arguments": --spring.config.location=<USER_HOME>\xmlstilling-ws-dev.yaml
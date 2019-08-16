# xmlstilling-ws
SOAP-basert WebService for å motta stillinger på hr-xml-format fra eksterne aktører.

##### Batch-kjøring
Applikasjonen kjører en skedulert ryddejobb for å slette meldinger (rader) i databasen som er mottatt for mer enn 6 mnd.
Se klassene som ligger i pakken `no.nav.xmlstilling.ws.batch` for intervaller og slettekriterie for mottatt dato.

##### Metrikker
Applikasjonen har metrikker for å telle antall meldinger som er mottatt OK `xmlstilling.mottatt.ok` og
mottatt med feil `xmlstilling.mottatt.feil`. Disse er tagget per leverandør.

### Kjøre DevApplication lokalt
Kopier application-dev.yaml til USER_HOME.

Åpne filen, og editer database-properties avhengig av om du ønsker å kjøre mot postgres i preprod eller lokal H2-database.

##### Alternativ 1 - postgresql

Kommenter inn properties for postgresql. For å sette brukernavn og passord, logg inn på https://vault.adeo.no og åpne
vault-CLI øverst til høyre i menylinjen. Kjør deretter kommando:

`vault read postgresql/preprod-sbs/creds/xmlstilling-ws-preprod-admin`

Kopier inn brukernavn og passord.

**NB:** Brukeren blir etter noe tid ugyldig og da må hele prosessen med kommando og kopiering
kjøres på nytt.

##### Alternativ 2 - H2

Kommenter inn properties for postgresql. Velg url avhengig av om du ønsker å kjøre med en in-mem- eller lokalt oppsatt instans.

##### Start app
* Velg "Edit configurations" for DevApplication
* Legg til i "Program arguments":

`--spring.config.location=<USER_HOME>\xmlstilling-ws-dev.yaml`

### Teste WS
Tjenesten kan testes på to måter, via SOAP-UI eller via curl. 
 
##### Test med SOAP-UI
* Installer SOAP-UI
* Velg "New SOAP Project"
* Gi prosjektet et navn og velg src/test/xmlstilling.wsdl som "Initial WSDL"
* Pass på at "Create Requests" er haket av.
* Åpne "Request 1"
* Endre url til miljøet du skal test mot. https://<MILJØ>/xmlstilling/SixSoap (evt. https://<MILJØ>/xmlstilling/v2/SixSoap). For å teste mot localhost, oppgi http://localhost:8080/SixSoap (evt /v2/SixSoap)
* Lim inn innholdet i src/test/stilling_for_xmlstilling.xml som melding i requesten
* For Authorization, velg "Basic"
* Legg inn brukernavn og passord. Liste over brukere og passord til testmiljø ligger i vault, secrets/secret/pam/xml-stilling/. For test mot localhost, bruk brukerA/pwdA.
* Kjør request og sjekk respons og evt database (via sql developer eller xmlstilling-admin).

##### Test med curl
`curl -u brukerA:pwdA -XPOST 'http://localhost:9022/SixSoap' -H 'Content-type: application/xml' --data-binary @src/test/resources/xml/example-globesoft.xml`

Det ligger flere testfiler under src/test/resources/xml/.

##### Respons fra tjenesten
Ok respons fra /xmlstilling/SixSoap vil se slik ut:

`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
   <soapenv:Body>
      <LeggInnStillingerResponse>
         <Id>
            <ns1:KlientReferanse xmlns:ns1="http://www.nav.no/nav_stilling_typer.xsd">KLIENT_DUMMY</ns1:KlientReferanse>
            <ns2:EksekveringOK xmlns:ns2="http://www.nav.no/nav_stilling_typer.xsd">OK_DUMMY</ns2:EksekveringOK>
            <ns3:Feilkode xmlns:ns3="http://www.nav.no/nav_stilling_typer.xsd">0</ns3:Feilkode>
            <ns4:Feilmelding xmlns:ns4="http://www.nav.no/nav_stilling_typer.xsd">MOTTATT</ns4:Feilmelding>
         </Id>
      </LeggInnStillingerResponse>
   </soapenv:Body>
</soapenv:Envelope>`

Ok respons fra /xmlstilling/v2/SixSoap vil se slik ut:

`<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
   <soapenv:Body>
      <LeggInnStillingerResponse>
         <Id>
            <ns1:KlientReferanse xmlns:ns1="http://www.nav.no/nav_stilling_typer.xsd">KLIENT_DUMMY</ns1:KlientReferanse>
            <ns2:EksekveringOK xmlns:ns2="http://www.nav.no/nav_stilling_typer.xsd">true</ns2:EksekveringOK>
            <ns3:Feilkode xmlns:ns3="http://www.nav.no/nav_stilling_typer.xsd">0</ns3:Feilkode>
            <ns4:Feilmelding xmlns:ns4="http://www.nav.no/nav_stilling_typer.xsd">MOTTATT</ns4:Feilmelding>
         </Id>
      </LeggInnStillingerResponse>
   </soapenv:Body>
</soapenv:Envelope>`

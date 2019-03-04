# xmlstilling-ws
SOAP-basert WebService for å motta stillinger på hr-xml-format fra eksterne aktører 

### Kjøre DevApplication lokalt
Kopier application-dev.yaml til USER_HOME og gi den nytt navn xmlstilling-ws-dev.yaml.
Slå opp database-properties i fasit og oppdater filen med dette.

Velg "Edit configurations" for DevApplication
Legg til i "Program arguments": --spring.config.location=<USER_HOME>\xmlstilling-ws-dev.yaml

### Teste tjeneste via SOAP-UI
Oppsett i SOAP-UI
* Velg "New SOAP Project"
* Gi prosjektet et navn og velg src/test/xmlstilling.wsdl som "Initial WSDL"
* Pass på at "Create Requests" er haket av.
* Åpne "Request 1"
* Endre url til miljøet du skal test mot. https://<MILJØ>/xmlstilling/SixSoap (evt. https://<MILJØ>/xmlstilling/v2/SixSoap). For å teste mot localhost, oppgi http://localhost:8080/SixSoap (evt /v2/SixSoap)
* Lim inn innholdet i src/test/stilling_for_xmlstilling.xml som melding i requesten
* For Authorization, velg "Basic"
* Legg inn brukernavn og passord. Liste over brukere og passord ligger i excelfil det lenkes til herfra: https://confluence.adeo.no/display/navnofor/xmlstilling+-+Utviklingsdokumentasjon (passordbeskyttet). For test mot localhost, bruk brukerA/pwdA.
* Kjør request og sjekk respons og evt database (via sql developer eller xmlstilling-admin).


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
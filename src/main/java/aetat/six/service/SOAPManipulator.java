package aetat.six.service;

import org.springframework.stereotype.Component;

/**
 * SOAPManipulator
 * <p/>
 * TO-DO: Denne klassen er laget for � kompensere for f�lgende forhold:
 * <p/>
 * 1) ARENA takler ikke diverse BP 1.0 verdier som AXIS 1.2 legger med i
 * SOAP-meldingen som AXIS produserer
 * <p/>
 * 2) AXIS 1.2 HAR egentlig st�tte for bakoverkompatibilitet med systemer
 * som ikke h�ndterer BP 1.0, ol men dette fungerer ikke p.t.
 * <p/>
 * Denne klassen er � betrakte som en midlertidig fix.
 * <p/>
 * *) N�r arena har tatt h�yde for BP 1.0 kompatibilitet m� denne klassen
 * fjernes
 *
 * @author VHA
 * @version 1.0
 */
@Component
public class SOAPManipulator {
    private static final char EMPTY_CHAR = ' ';
    private static final char RIGHT_BRACE = '>';
    private static final char END_TAG = '/';
    public static final char GAASE_TEGN = '"';

    /**
     * cleanSoapString
     *
     * @param soapxml String
     * @return String
     */
    public static String cleanSoapString(String soapxml, String elementToRemove) {
        if (soapxml == null) {
            return soapxml;
        }

        StringBuffer resXml = new StringBuffer(soapxml);
        int startPos = 0;

        while (true) {
            startPos = resXml.indexOf(elementToRemove, startPos);
            if (startPos == -1) {
                break;
            } else {
                int sluttPos = -1;
                boolean inExternalDeclaration = false;

                for (int i = startPos; i < resXml.length(); i++) {
                    char nextChar = resXml.charAt(i);

                    if (nextChar == GAASE_TEGN) {
                        inExternalDeclaration = !inExternalDeclaration;
                    }

                    if (nextChar == RIGHT_BRACE || nextChar == EMPTY_CHAR || (!inExternalDeclaration && nextChar == END_TAG)) {
                        sluttPos = i;
                        break;
                    }
                }

                if (sluttPos != -1) {
                    resXml = new StringBuffer(resXml.substring(0, startPos).trim() + resXml.substring(sluttPos));
                }
            }
        }

        return resXml.toString();
    }
}

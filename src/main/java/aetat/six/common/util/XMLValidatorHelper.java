package aetat.six.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class XMLValidatorHelper extends DefaultHandler {

    private static final Logger logger = LoggerFactory.getLogger(XMLValidatorHelper.class);

    private SAXParser sp;

    public XMLValidatorHelper() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            sp = spf.newSAXParser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isWellFormed(String xml) {
        try {
            return isWellFormed(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding er ikke støttet", e.getMessage());
            return false;
        }
    }

    public boolean isWellFormed(InputStream xml) {
        try {
            XMLReader xmlReader = sp.getXMLReader();
            xmlReader.setErrorHandler(this);
            xmlReader.parse(new InputSource(xml));
            return true;
        } catch (SAXParseException e) {
            logger.warn("XML-formatet på melding er ikke korrekt.", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.warn("Ukjent feil oppstod under lesing av soap-melding.", e.getMessage());
            return false;
        }
    }
}

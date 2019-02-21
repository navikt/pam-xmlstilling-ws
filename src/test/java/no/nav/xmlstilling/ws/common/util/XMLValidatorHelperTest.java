package no.nav.xmlstilling.ws.common.util;

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;

public class XMLValidatorHelperTest extends TestCase {

	private XMLValidatorHelper xmlValidatorHelper;
	private String validXmlString;
	private String xmlForFlereStillinger;

	public void setUp() throws Exception {
        validXmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<outerTag>" +
                "  <innerTag1>yes</innerTag1>" +
                "  <innerTag2>" +
                "    nonono med æø" +
                "  </innerTag2>" +
                "</outerTag>";
		xmlValidatorHelper = new XMLValidatorHelper( );
		xmlForFlereStillinger = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("xml/ok__stillinger_for_ws-innsending_x2.xml"));
	}
	
	public void testHappyCaseXmlForFlereStillingerValiderer() {
		assertTrue(xmlValidatorHelper.isWellFormed(xmlForFlereStillinger));
	}

	public void testWellFormedXmlValiderer() {
		assertTrue(xmlValidatorHelper.isWellFormed(validXmlString));
	}

	public void testNotWellFormedXmlValidererIkke() {
		assertFalse(xmlValidatorHelper.isWellFormed("<badxml>"));
	}

	public void testWellFormedXmlAsReaderValiderer() {
		ByteArrayInputStream validXmlReader = new ByteArrayInputStream(validXmlString.getBytes());
		assertTrue(xmlValidatorHelper.isWellFormed(validXmlReader));
	}
}
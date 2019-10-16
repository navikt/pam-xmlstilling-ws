package no.nav.xmlstilling.ws.web.transport.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

class ContentParsers {

    interface ContentParser {

        String parse(byte[] content);

    }

    private Logger logger = LoggerFactory.getLogger(ContentParsers.class);

    private static ContentParser UTF_8_PARSER = content -> new String(content, StandardCharsets.UTF_8);
    private static ContentParser ISO_8859_1_PARSER = content -> new String(content, StandardCharsets.ISO_8859_1);

    private static List<String> UTF_8_USERS = Arrays.asList("karriereno", "mynetwork", "stepstone", "webcruiter");
    private static List<String> ISO_8859_1_USERS = Arrays.asList("globesoft", "hrmanager", "jobbnorge");

    ContentParser forCompany(String eksterntBrukerNavn) {

        if (UTF_8_USERS.contains(eksterntBrukerNavn)) {
            logger.info("Using UTF_8_PARSER for " + eksterntBrukerNavn);
            return UTF_8_PARSER;
        }
        if (ISO_8859_1_USERS.contains(eksterntBrukerNavn)) {
            logger.info("Using ISO_8859_1_PARSER for " + eksterntBrukerNavn);
            return ISO_8859_1_PARSER;
        }

        logger.info("Using UTF_8_PARSER for unknown user, " + eksterntBrukerNavn);
        return UTF_8_PARSER;
    }
}

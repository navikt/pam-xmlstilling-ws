package aetat.six.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

public class ConverterUtils {

    private static final Logger logger = LoggerFactory.getLogger(ConverterUtils.class);

    private ConverterUtils() {
        //hide default constructor
    }

    public static String read(BufferedReader bf) {

        StringBuffer sbf = new StringBuffer("");
        String str;
        try {
            str = bf.readLine();
            while (str != null) {
                sbf.append(str + "\n");
                str = bf.readLine();
            }
        } catch (IOException e) {
            logger.debug("", e);
        }
        return sbf.toString();
    }

}

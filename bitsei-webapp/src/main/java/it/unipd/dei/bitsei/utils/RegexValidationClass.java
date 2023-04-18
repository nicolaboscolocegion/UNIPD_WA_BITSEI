package it.unipd.dei.bitsei.utils;

import org.apache.logging.log4j.message.StringFormattedMessage;

import java.util.regex.Pattern;

/**
 * Utils class for regEx validation of input fields.
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public abstract class RegexValidationClass {

    public static void fieldRegexValidation(String regexPattern, String emailAddress, String type) {
        if(!Pattern.compile(regexPattern).matcher(emailAddress).matches()) {
            throw new IllegalArgumentException(String.valueOf(new StringFormattedMessage("%s : format not valid", type)));
        }
    }
}

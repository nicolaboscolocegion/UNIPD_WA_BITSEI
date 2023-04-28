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

    /**
     * Empty constructor.
     */
    public RegexValidationClass(){
        // empty constructor
    }

    /**
     * fieldRegexValidation method validates the input fields of the user.
     *
     * @param regexPattern the regex pattern to be matched
     * @param emailAddress the email address to be validated
     * @param type         the type of the field to be validated
     * @throws IllegalArgumentException if the input fields are not valid
     */
    public static void fieldRegexValidation(String regexPattern, String emailAddress, String type) {
        if (!Pattern.compile(regexPattern).matcher(emailAddress).matches()) {
            throw new IllegalArgumentException(String.valueOf(new StringFormattedMessage("%s : format not valid", type)));
        }
    }
}

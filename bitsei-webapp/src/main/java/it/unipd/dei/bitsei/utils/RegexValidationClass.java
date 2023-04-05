package it.unipd.dei.bitsei.utils;

import org.apache.logging.log4j.message.StringFormattedMessage;

import java.util.regex.Pattern;

public abstract class RegexValidationClass {

    public static void fieldRegexValidation(String regexPattern, String emailAddress, String type) {
        if(!Pattern.compile(regexPattern).matcher(emailAddress).matches()) {
            throw new IllegalArgumentException(String.valueOf(new StringFormattedMessage("%s : format not valid", type)));
        }
    }
}

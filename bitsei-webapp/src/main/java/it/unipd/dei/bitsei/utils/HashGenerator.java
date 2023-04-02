package it.unipd.dei.bitsei.utils;

public class HashGenerator {

    // Generate a random hash
    public static String generateHash() {
        return java.util.UUID.randomUUID().toString();
    }
}
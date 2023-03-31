package it.unipd.dei.bitsei.resources;


/**
 * Contains constants for the actions performed by the application.
 */
public final class Actions {

    /**
     * The list of the users
     */
    public static final String LIST_USER = "LIST_USER";

    /**
     * login of the user
     */
     public static final String LOGIN = "LOGIN";


    /**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}

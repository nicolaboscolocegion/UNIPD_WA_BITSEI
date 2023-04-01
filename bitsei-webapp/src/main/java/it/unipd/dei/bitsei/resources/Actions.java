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
     * The rest password
     */
    public static final String REST_PASSWORD = "REST_PASSWORD";

    /**
     * The change password
     */
    public static final String CHANGE_PASSWORD = "CHANGE_PASSWORD";

    /**

     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}

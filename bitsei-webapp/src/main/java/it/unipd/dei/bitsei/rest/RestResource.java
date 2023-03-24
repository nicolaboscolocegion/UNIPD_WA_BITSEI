package it.unipd.dei.bitsei.rest;

import java.io.IOException;

/**
 * Represents a generic REST resource. The {@link #serve()} method handles the request served by this REST resource.
 */
public interface RestResource {

    /**
     * Serves a REST request.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    void serve() throws IOException;

}
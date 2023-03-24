package it.unipd.dei.bitsei.dao;

import java.sql.SQLException;

/**
 * Represents a generic Data Access Object (DAO). The {@link #access()} method performs the actual access to the
 * database, while the {@link #getOutputParam()} allows for retrieving any output parameters resulting from the access,
 * if any.
 *
 * @param <T> the type of the output parameter returned by the DAO.
 */
public interface DataAccessObject<T> {

    /**
     * Accesses the database.
     *
     * @return a reference to this {@code DataAccessObject} object.
     * @throws SQLException if something goes wrong while accessing the database.
     */
    DataAccessObject<T> access() throws SQLException;

    /**
     * Retrieves any output parameters, after the access to the database.
     *
     * @return output parameter, or @code{null} if there is no output parameter.
     */
    T getOutputParam();

}

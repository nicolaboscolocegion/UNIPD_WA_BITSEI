package it.unipd.dei.bitsei.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides support for the actual implementation of a DAO object.
 *
 * <p>
 * {@code AbstractDAO} is designed to achieve the following goals:
 * <ol>
 * <li>a consistent way of accessing the database and managing exceptions is
 * guaranteed for all of the classes that need to directly interact with the
 * DBMS.
 * <li>independence from any particular DBMS is achieved, meaning that the
 * DBMS-dependent code is as isolated as possible and that upper layers can
 * transparently change the underlying DBMS, without the need of modifying their
 * code.
 * </ol>
 *
 * <p>
 * With respect to the first goal, {@code AbstractDAO}
 * provides a basic infrastructure which manages the connection, the commitment
 * of transactions (or the roll-back), if needed, and any exception that may be
 * thrown while accessing the database.
 *
 * <p>
 * With respect to the second goal,{@code AbstractDAO} is the
 * root of a hierarchy of helper classes, which isolate as much as possible the
 * DBMS-dependent code.
 *
 *
 * <p>
 * Concrete subclasses have to implement the abstract method
 * {@link #doAccess()} in order to define the needed
 * logic for accessing the database. This method is, in turn, called by
 * {@link AbstractDAO#access()}, which provides a consistent way of
 * accessing the database and managing exceptions.
 *
 * <p>
 * Concrete subclasses may also override {@link AbstractDAO#access()}
 * in order to provide a
 * different management of connection and transaction if they have peculiar
 * needs. Nevertheless, the objective of this class is to ease sub-classes by
 * providing a pre-packed logic for connection and transaction management, that
 * is what {@link AbstractDAO#access()} does.
 *
 * @param <T> the type of the output parameter returned by the DAO.
 */
public abstract class AbstractDAO<T> implements DataAccessObject<T> {

    /**
     * A LOGGER available for all the subclasses.
     */
    protected static final Logger LOGGER = LogManager.getLogger(AbstractDAO.class,
            StringFormatterMessageFactory.INSTANCE);

    /**
     * The connection to be used to access the database.
     */
    protected final Connection con;

    /**
     * The output parameter, if any
     */
    protected T outputParam = null;

    /**
     * Indicates whether the database has been already accessed or not.
     */
    private boolean accessed = false;

    /**
     * A lock for synchronization.
     */
    private final Object lock = new Object();

    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    protected AbstractDAO(final Connection con) {

        if (con == null) {
            LOGGER.error("The connection cannot be null.");
            throw new NullPointerException("The connection cannot be null.");
        }
        this.con = con;

        try {
            // ensure that autocommit is true
            con.setAutoCommit(true);
            LOGGER.debug("Auto-commit set to default value true.");
        } catch (final SQLException e) {
            LOGGER.warn("Unable to set connection auto-commit to true.", e);
        }

    }

    public final DataAccessObject<T> access() throws SQLException {

        synchronized (lock) {
            try {
                if (accessed) {
                    LOGGER.error("Cannot use a DataAccessObject more than once.");
                    throw new SQLException("Cannot use a DataAccessObject more than once.");
                }
            } finally {
                accessed = true;
            }
        }

        try {
            doAccess();

            try {
                con.close();
                LOGGER.debug("Connection successfully closed.");
            } catch (final SQLException e) {
                LOGGER.error("Unable to close the connection to the database.", e);
                throw e;
            }
        } catch (final Throwable t) {

            LOGGER.error("Unable to perform the requested database access operation.", t);

            try {
                if (!con.getAutoCommit()) {
                    // autoCommit == false => transaction needs to be rolled back
                    con.rollback();
                    LOGGER.info("Transaction successfully rolled-back.");
                }
            } catch (final SQLException e) {
                LOGGER.error("Unable to roll-back the transaction.", e);
            } finally {
                try {
                    con.close();
                    LOGGER.debug("Connection successfully closed.");
                } catch (final SQLException e) {
                    LOGGER.error("Unable to close the connection to the database.", e);
                }

            }

            if (t instanceof SQLException) {
                throw (SQLException) t;
            } else {
                throw new SQLException("Unable to perform the requested database access operation.", t);
            }
        }

        return this;
    }


    @Override
    public final T getOutputParam() {

        synchronized (lock) {
            if (!accessed) {
                LOGGER.error("Cannot retrieve the output parameter before accessing the database.");
                throw new IllegalStateException("Cannot retrieve the output parameter before accessing the database.");
            }
        }

        return outputParam;
    }

    /**
     * Performs the actual logic needed for accessing the database.
     * <p>
     * Subclasses have to implement this method in order to define the
     * actual strategy for accessing the database.
     *
     * @throws Exception if there is any issue.
     */
    protected abstract void doAccess() throws Exception;


}

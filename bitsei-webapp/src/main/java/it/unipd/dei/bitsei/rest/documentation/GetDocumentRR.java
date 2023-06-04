package it.unipd.dei.bitsei.rest.documentation;

import it.unipd.dei.bitsei.dao.documentation.GetDocumentDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A REST resource for getting document.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class GetDocumentRR extends AbstractRR {
    private final int company_id;
    private final int invoice_id;
    private final int document_type;
    private final String absPath;

    /**
     * Creates a new REST resource for listing {@code Company}s.
     *
     * @param req           the HTTP request.
     * @param res           the HTTP response.
     * @param con           the connection to the database.
     * @param absPath       the absolute path of the project
     * @param company_id    the id of the company
     * @param invoice_id    the id of the invoice
     * @param document_type the type of the document
     */
    public GetDocumentRR(final HttpServletRequest req, final HttpServletResponse res, Connection con, final String absPath, final int company_id, final int invoice_id, final int document_type) {
        super(Actions.GET_DOCUMENT, req, res, con);
        this.company_id = company_id;
        this.invoice_id = invoice_id;
        this.document_type = document_type;
        this.absPath = absPath;
    }

    //uri format /rest/getdocument/{0/1/2}/invoice/{id}/company/{id}
    @Override
    protected void doServe() throws IOException {

        String filename = null;
        Message m = null;

        try {

            int owner_id = Integer.parseInt(req.getSession().getAttribute("owner_id").toString());
            if (document_type < 0 || document_type > 2) {
                throw new IllegalArgumentException();
            }

            filename = new GetDocumentDAO(con, company_id, owner_id, invoice_id, document_type).access().getOutputParam();


            if (filename != null) {
                LOGGER.info("Document path successfully get.");
                File f = null;
                if (document_type != 2) {
                    f = new File(absPath + "/pdf/" + filename);
                    res.setContentType("application/pdf");
                } else {
                    f = new File(absPath + "/xml/IT" + filename);
                    res.setContentType("application/xml");
                }
                res.setStatus(HttpServletResponse.SC_OK);
                res.getOutputStream().write(FileUtils.readFileToByteArray(f));

            } else {
                LOGGER.error("Fatal error while fetching document path.");
                m = new Message("Cannot fetch document name: make sure you have a right access to this company.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list Companies: unexpected database error.", ex.getMessage());
            m = new Message("Cannot list Companies: unexpected database error.", "E5A1", "");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch (NumberFormatException ex) {
            m = new Message("IDs in URI not parsable.", "E5A1", "");
            LOGGER.info("IDs in URI not parsable");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (RuntimeException e) {
            LOGGER.info("Runtime exception: " + e.getStackTrace());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

}

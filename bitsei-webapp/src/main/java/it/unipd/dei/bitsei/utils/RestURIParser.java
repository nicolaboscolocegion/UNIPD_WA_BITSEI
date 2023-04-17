package it.unipd.dei.bitsei.utils;


/**
 * Utils class for representing and using Bitsei rest URI
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class RestURIParser {

    private String URI;
    private String resource;
    private int resourceID;
    private int companyID;
    private RestURIParser() {
        this.URI = URI;
    }

    public RestURIParser parseURI(String URI) {
        String[] parts = URI.split("/");
        if (parts[0] != "rest") {
            throw new IllegalArgumentException("URI NOT RESTFUL");
        }

        if (parts.length < 2) {
            throw new IllegalArgumentException("URI WITHOUT RESOURCE SPECIFIED");
        }
        else {
            this.resource = parts[1];
        }

        try {
            resourceID = Integer.parseInt(parts[2]);
            if (parts[3] != "company") {
                throw new IllegalArgumentException("NO COMPANY PROPERTY FOUND.");
            }
            else {
                try {
                    companyID = Integer.parseInt(parts[4]);
                }
                catch ( NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }


        } catch (NumberFormatException ex) {
            if (parts[2] != "company") {
                throw new IllegalArgumentException("NO COMPANY PROPERTY FOUND.");
            }
            else  try {
                companyID = Integer.parseInt(parts[3]);
            }
            catch ( NumberFormatException exx) {
                exx.printStackTrace();
            }
        }

        return this;
    }

    public String getResource() {
        return resource;
    }

    public int getResourceID() {
        return resourceID;
    }

    public int getCompanyID() {
        return companyID;
    }
}

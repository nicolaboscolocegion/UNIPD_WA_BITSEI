package it.unipd.dei.bitsei.utils;


/**
 * Utils class for representing and using Bitsei rest URI
 *
 * @author Mirco Cazzaro (mirco.cazzaro@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class RestURIParser {

    /**
     * URI of the rest request
     */
    private String URI;

    /**
     * Resource of the rest request
     */
    private String resource;

    /**
     * Resource ID of the rest request
     */
    private int resourceID;

    /**
     * Company ID of the rest request
     */
    private int companyID;

    /**
     * Parses the URI of the rest request
     * @param URI the URI of the rest request
     */
    public RestURIParser(String URI) {
        this.URI = URI;

        String[] parts = URI.split("/");

        if (!parts[2].equals("rest")) {
            throw new IllegalArgumentException("URI NOT RESTFUL");
        }

        if (parts.length < 6) {
            throw new IllegalArgumentException("TOO FEW ARGUMENTS IN URI");
        }
        else {
            resource = parts[3];
        }

        try {
            resourceID = Integer.parseInt(parts[4]);
            if (!parts[5].equals("company")) {
                throw new IllegalArgumentException("NO COMPANY PROPERTY FOUND.");
            }
            else {
                try {
                    companyID = Integer.parseInt(parts[6]);
                }
                catch ( NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }


        } catch (NumberFormatException ex) {
            resourceID = -1;
            if (!parts[4].equals("company")) {
                throw new IllegalArgumentException("NO COMPANY PROPERTY FOUND.");
            }
            else  try {
                companyID = Integer.parseInt(parts[5]);
            }
            catch ( NumberFormatException exx) {
                exx.printStackTrace();
            }
        }
    }

    /**
     * gets the URI of the rest request
     * @return the URI of the rest request
     */
    public String getURI() {
        return URI;
    }

    /**
     * gets the resource of the rest request
     * @return the resource of the rest request
     */
    public String getResource() {
        return resource;
    }

    /**
     * gets the resource ID of the rest request
     * @return the resource ID of the rest request
     */
    public int getResourceID() {
        return resourceID;
    }

    /**
     * gets the company ID of the rest request
     * @return the company ID of the rest request
     */
    public int getCompanyID() {
        return companyID;
    }

    /**
     * gets the string representation of the rest request
     * @return the string representation of the rest request
     */
    @Override
    public String toString(){
        return "URL: res:" + this.getResource() + " -  res_id:" + getResourceID() + " -  com_id:" + getCompanyID();
    }
}

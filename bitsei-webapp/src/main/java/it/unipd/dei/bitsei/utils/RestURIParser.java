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


    public String getURI() {
        return URI;
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

    @Override
    public String toString(){
        return "URL: res:" + this.getResource() + " -  res_id:" + getResourceID() + " -  com_id:" + getCompanyID();
    }
}

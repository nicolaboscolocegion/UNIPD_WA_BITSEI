package it.unipd.dei.bitsei.resources;

import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;


/**
 * login resurce for authenticate the user
 */
public class LoginResurce extends AbstractResource {

    private String email;
    private String password;
    /**
     * 
     * @param email email of the user
     * @param password password of the user
     */
    public LoginResurce(String email, String password){
        this.email = email;
        this.password = password;
    }

    /**
     * create the resurce from a Json
     * @param in input stream with json file
     */
    public static LoginResurce fromJSON(final InputStream in){
        String jemail=null, jpassword=null;

        try{
            final JsonParser jp = JSON_FACTORY.createParser(in);
            
            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "email":
                            jp.nextToken();
                            jemail = jp.getText();
                            break;
                        case "password":
                            jp.nextToken();
                            jpassword = jp.getText();
                            break;
                    }
                }
            }

        }catch(Exception e){
            LOGGER.error("Unable to parse an User object from JSON.", e);
            
        }


        

        return new LoginResurce(jemail, jpassword);
    }

    @Override
    protected void writeJSON(OutputStream out) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("");
    }
    
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
}
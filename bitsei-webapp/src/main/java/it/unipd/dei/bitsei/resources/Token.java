package it.unipd.dei.bitsei.resources;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerator;

public class Token extends AbstractResource {

    private final String token;
    
    public Token (String T){
        this.token=T;
    }

    public String getToken() {
        return token;
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        
        jg.writeStartObject();
        

        jg.writeStringField("token", token);
        jg.writeEndObject();
        jg.flush();
    }
    
}

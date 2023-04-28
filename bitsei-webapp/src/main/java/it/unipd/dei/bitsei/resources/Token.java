/*
 * Copyright 2022-2023 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.unipd.dei.bitsei.resources;

import java.io.IOException;
import java.io.OutputStream;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Represents a token.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class Token extends AbstractResource {

    private final String token;

    /**
     * Build a new Token object
     * @param T the BEARER token as string
     */
    public Token (String T){
        this.token=T;
    }

    /**
     * Get the BEARER token
     * @return the BEARER token
     */
    public String getToken() {
        return token;
    }

    /**
     * Write JSON token data
     * @param out The output stream
     * @throws Exception if error occurs
     */
    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeStringField("token", token);
        jg.writeEndObject();

        jg.flush();
    }
    
}

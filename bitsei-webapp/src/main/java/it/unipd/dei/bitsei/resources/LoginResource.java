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

import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;


/**
 * login resource for authenticate the user
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class LoginResource extends AbstractResource {

    private final String email;
    private final String password;

    /**
     * @param email    email of the user
     * @param password password of the user
     */
    public LoginResource(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * create the resource from a Json
     *
     * @param in input stream with json file
     */
    public static LoginResource fromJSON(final InputStream in) {
        String jemail = null;
        String jPassword = null;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "email" -> {
                            jp.nextToken();
                            jemail = jp.getText();
                        }
                        case "password" -> {
                            jp.nextToken();
                            jPassword = jp.getText();
                        }
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error("Unable to parse an User object from JSON.", e);

        }


        return new LoginResource(jemail, jPassword);
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
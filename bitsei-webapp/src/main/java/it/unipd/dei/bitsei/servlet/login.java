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


package it.unipd.dei.bitsei.servlet;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import it.unipd.dei.bitsei.resources.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;


/**
 * Controll the autetication of the user
 *
 * @author Nicola Boscolo
 * @version 1.00
 * @since 1.00
 */
public class login extends AbstractDatabaseServlet{

    /**
	 * Controll the autetication of the user
	 *
	 * @param req the HTTP request from the client.
	 * @param res the HTTP response from the server.
	 *
	 * @throws IOException if any error occurs in the client/server communication.
	 */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        
        String username; //username of the user
        String encPassword; //encripthed password
        String password; //clear password of the user

        Message m; //post message

        boolean auteincate =false; //autentication flag

        try {

			// retrieves the username paramiters

			//TODO take username from post
            //username =
            
            //TODO take encPassword from post
            //encPassword =

			// creates a new object for accessing the database and searching the employees
			
        } catch (SQLException ex) {
			m = new Message("Cannot search for user: unexpected error while accessing the database.", "E200",
					ex.getMessage());

			LOGGER.error("Cannot search for employees: unexpected error while accessing the database.", ex);
		}

        //send the token and the message if the user is autenticated
    }
}


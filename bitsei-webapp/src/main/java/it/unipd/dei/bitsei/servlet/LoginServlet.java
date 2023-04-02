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
import org.jose4j.lang.JoseException;

import it.unipd.dei.bitsei.dao.UserAuthDAO;
 import it.unipd.dei.bitsei.resources.Actions;
 import it.unipd.dei.bitsei.resources.LogContext;
 import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.resources.TokenJWT;

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
 public class LoginServlet extends AbstractDatabaseServlet{
 
	 /**
	  * Controll the autetication of the user
	  *
	  * @param req the HTTP request from the client.
	  * @param res the HTTP response from the server.
	  *
	  * @throws IOException if any error occurs in the client/server communication.
	  */
	 public void doPost(HttpServletRequest req, HttpServletResponse res)  {
		 
		LogContext.setIPAddress(req.getRemoteAddr());
		LogContext.setResource(req.getRequestURI());
		LogContext.setAction("LOGIN");
 
		 
		//username of the user
		String username = null; 
		 
		//password in clear of the username, this will changend when doing the frontend
		  
		String password = null; 
 
		 
		//Response 
		Message m; 
 
		boolean autenticate =false; //autentication flag
 
		try {

				// retrieves the user paramiters
				
				username = req.getParameter("username");
				password = req.getParameter("password");


				// creates a new object for accessing the database and searching the employees
				
				autenticate = new UserAuthDAO(getConnection(), username, password).access().getOutputParam();


			} catch (SQLException ex) {
				m = new Message("Cannot search for user: unexpected error while accessing the database.", "E200",
						ex.getMessage());

				LOGGER.error("Cannot search for user: unexpected error while accessing the database.", ex);
			}
			
			
			
			try {
				// set the MIME media type of the response
				res.setContentType("text/html; charset=utf-8");

				if (autenticate){
				TokenJWT token = new TokenJWT(username, password);
				res.addHeader("Authorization", "Bearer "+token.getTokenString());

				
				}

				// get a stream to write the response
				PrintWriter out = res.getWriter();

				// write the HTML page
				out.printf("<!DOCTYPE html>%n");

				out.printf("<html lang=\"en\">%n");
				out.printf("<head>%n");
				out.printf("<meta charset=\"utf-8\">%n");
				out.printf("<title>Auth</title>%n");
				out.printf("</head>%n");

				out.printf("<body>%n");

				if(autenticate){
					out.printf("<h1>Autenticate</h1>%n");
					out.printf("<hr>%n");
				}else{
					out.printf("<h1>NOT Autenticate</h1>%n");
					out.printf("<hr>%n");
				}


				out.printf("</body>%n");

				out.printf("</html>%n");

				// flush the output stream buffer
				out.flush();

				// close the output stream
				out.close();
			} catch (IOException | JoseException ex) {
				LOGGER.error(new StringFormattedMessage("Unable to send response when Autenticate"), ex);
				
			} finally {
				LogContext.removeIPAddress();
				LogContext.removeAction();
				LogContext.removeUser();
			}
		}
 
		 //send the token and the message if the user is autenticated
}
 
 
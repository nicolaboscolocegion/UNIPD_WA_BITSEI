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


package it.unipd.dei.bitsei.rest;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.jose4j.lang.JoseException;

import it.unipd.dei.bitsei.dao.UserAuthDAO;
import it.unipd.dei.bitsei.resources.Actions;
import it.unipd.dei.bitsei.resources.LogContext;
import it.unipd.dei.bitsei.resources.LoginResurce;
import it.unipd.dei.bitsei.resources.Message;
import it.unipd.dei.bitsei.resources.ResourceList;
import it.unipd.dei.bitsei.resources.Token;
import it.unipd.dei.bitsei.servlet.AbstractDatabaseServlet;
import it.unipd.dei.bitsei.utils.TokenJWT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
 
 
 
/**
 * Controll the autetication of the user
*
* @author Nicola Boscolo
* @version 1.00
* @since 1.00
*/
public class LoginUserRR extends AbstractRR{
 

	public LoginUserRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(Actions.LOGIN, req, res, con);
    }

	

	

	/**
	 * Controll the autetication of the user
	*
	* 
	* 
	*
	* @throws IOException if any error occurs in the client/server communication.
	*/
	@Override
	protected void doServe() throws IOException{
		
	//username of the user
	String email = null; 
		
	//password in clear of the username, this will changend when doing the frontend
		
	String password = null; 

		
	//Response 
	Message m; 

	boolean autenticate =false; //autentication flag

	try {

			// retrieves the user paramiters
				
			 LoginResurce	user = LoginResurce.fromJSON(req.getInputStream());
			


			// creates a new object for accessing the database and searching the employees
			
			autenticate = new UserAuthDAO(con, user).access().getOutputParam();


	} catch (SQLException ex) {
		m = new Message("Cannot search for user: unexpected error while accessing the database.", "E200",
				ex.getMessage());

		LOGGER.error("Cannot search for user: unexpected error while accessing the database.", ex);
	}catch(IOException e){
		LOGGER.error("unable to read request inputStream", e);
	}
		
		
		
		try {
			// set the MIME media type of the response
			res.setContentType("application/json; charset=utf-8");

			if (autenticate){
			TokenJWT token = new TokenJWT(email, password);
			
			res.setStatus(HttpServletResponse.SC_OK);
			new Token(token.getTokenString()).toJSON(res.getOutputStream());

			}
			
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


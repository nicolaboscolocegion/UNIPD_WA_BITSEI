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
package it.unipd.dei.bitsei.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unipd.dei.bitsei.utils.TokenJWT;


/**
 * Filter that checks if the request contains a valid JWT token.
 * If the token is valid, the filter adds the user email to the session.
 * If the token is not valid, the filter returns a 401 status code.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class JwtAuthenticationFilter implements Filter {

    // logger for this class
    private static final java.util.logging.Logger LOG = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    // header key for the Authorization header -> "Authorization"
    private static final String AUTH_HEADER_KEY = "Authorization";

    // prefix for the value of the Authorization header (with trailing space) -> "Bearer "
    private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer "; // with trailing space to separate token

    // HTTP status code for "Unauthorized" (401)
    private static final int STATUS_CODE_UNAUTHORIZED = 401;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("JwtAuthenticationFilter initialized");
    }

    /**
     * This method is called for every request
     *
     * @param servletRequest  The request to filter
     * @param servletResponse The response to filter
     * @param filterChain     The chain of filters to apply
     */
    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        try {
            TokenJWT token_jwt = new TokenJWT(getBearerToken(httpRequest));

            if (token_jwt.getIsValid() == TokenJWT.NOT_VALID) {
                throw new Exception("Invalid JWT token in request");
            }

            httpRequest.getSession().setAttribute("email", token_jwt.getEmail());
            httpRequest.getSession().setAttribute("owner_id", token_jwt.getOwnerID());

            filterChain.doFilter(httpRequest, servletResponse);
        } catch (final Exception e) {
            LOG.log(Level.WARNING, "Failed logging in with security token", e);
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setContentLength(0);
            httpResponse.setStatus(STATUS_CODE_UNAUTHORIZED);
        }
    }

    // This method is only called once at the end of the application lifecycle
    @Override
    public void destroy() {
        LOG.info("JwtAuthenticationFilter destroyed");
    }

    /**
     * Get the bearer token from the HTTP request.
     * The token is in the HTTP request "Authorization" header in the form of: "Bearer [token]"
     */
    private String getBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER_KEY);
        if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
            return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
        }
        return null;
    }
}
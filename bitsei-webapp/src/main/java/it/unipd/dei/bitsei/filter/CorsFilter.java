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
import java.util.logging.Logger;


/**
 * Filter that manages CORS. It adds the required headers to the response.
 * It also manages the preflight request. -> https://developer.mozilla.org/en-US/docs/Glossary/Preflight_request
 * Preflight requests are automatically handled by the browser, but the server must respond to them.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class CorsFilter implements Filter {
    /**
     * Default constructor
     */
    public CorsFilter() {
    }

    // logger for this class
    private static final java.util.logging.Logger LOG = Logger.getLogger(JwtAuthenticationFilter.class.getName());


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("CorsFilter initialized");
    }

    /**
     * This method is called for every request
     *
     * @param servletRequest  The request to filter
     * @param servletResponse The response to filter
     * @param filterChain     The chain of filters to apply
     */
    @Override
    public void doFilter(
            final ServletRequest servletRequest,
            final ServletResponse servletResponse,
            final FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Authorization, X-Requested-With, Content-Type, Accept, Key");

        // Cors filter for the preflight request
        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) servletRequest).getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(httpRequest, servletResponse);
    }
}
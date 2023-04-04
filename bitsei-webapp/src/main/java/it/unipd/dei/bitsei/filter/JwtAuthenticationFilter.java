package it.unipd.dei.bitsei.filter;

import it.unipd.dei.bitsei.resources.TokenJWT;
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


public class JwtAuthenticationFilter implements Filter {

    private static final java.util.logging.Logger LOG = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer "; // with trailing space to separate token

    private static final int STATUS_CODE_UNAUTHORIZED = 401;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("JwtAuthenticationFilter initialized");
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        try {
            TokenJWT token_jwt = new TokenJWT(getBearerToken(httpRequest));

            if (token_jwt.getIsValid() == TokenJWT.NOT_VALID) {
                throw new Exception("Invalid JWT");
            }

            httpRequest.getSession().setAttribute("email", token_jwt.getEmail());

            filterChain.doFilter(httpRequest, servletResponse);
        } catch (final Exception e) {
            LOG.log(Level.WARNING, "Failed logging in with security token", e);
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setContentLength(0);
            httpResponse.setStatus(STATUS_CODE_UNAUTHORIZED);
        }
    }

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
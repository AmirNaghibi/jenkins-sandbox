package org.nowherehub.mcr1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class KeycloakAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";


    public static final RequestMatcher DEFAULT_REQUEST_MATCHER = new RequestHeaderRequestMatcher(AUTHORIZATION_HEADER);

    private static final Logger log = LoggerFactory.getLogger(KeycloakAuthenticationProcessingFilter.class);

    private ApplicationContext applicationContext;

    private AuthenticationManager authenticationManager;

    //TODO hardcoded value
    private String jwkSetUrl = "http://localhost:19191/auth/realms/master/protocol/openid-connect/certs";

    /**
     * Creates a new Keycloak authentication processing filter with given {@link AuthenticationManager} and the
     * {@link KeycloakAuthenticationProcessingFilter#DEFAULT_REQUEST_MATCHER default request matcher}.
     *
     * @param authenticationManager the {@link AuthenticationManager} to authenticate requests (cannot be null)
     * @see KeycloakAuthenticationProcessingFilter#DEFAULT_REQUEST_MATCHER
     */
    public KeycloakAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
        this(authenticationManager, DEFAULT_REQUEST_MATCHER);

    }

    /**
     * Creates a new Keycloak authentication processing filter with given {@link AuthenticationManager} and
     * {@link RequestMatcher}.
     * <p>
     * Note: the given request matcher must support matching the <code>Authorization</code> header if
     * bearer token authentication is to be accepted.
     * </p>
     *
     * @param authenticationManager                the {@link AuthenticationManager} to authenticate requests (cannot be null)
     * @param requiresAuthenticationRequestMatcher the {@link RequestMatcher} used to determine if authentication
     *                                             is required (cannot be null)
     * @see RequestHeaderRequestMatcher
     * @see OrRequestMatcher
     */
    public KeycloakAuthenticationProcessingFilter(AuthenticationManager authenticationManager, RequestMatcher
            requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        this.authenticationManager = authenticationManager;
        super.setAuthenticationManager(authenticationManager);
        super.setAllowSessionCreation(false);
        super.setContinueChainBeforeSuccessfulAuthentication(false);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.debug("Attempting Keycloak authentication");

//        HttpFacade facade = new SimpleHttpFacade(request, response);
//        KeycloakDeployment deployment = adapterDeploymentContext.resolveDeployment(facade);
//
//        // using Spring authenticationFailureHandler
//        deployment.setDelegateBearerErrorResponseSending(true);
//
//        AdapterTokenStore tokenStore = adapterTokenStoreFactory.createAdapterTokenStore(deployment, request);
//        RequestAuthenticator authenticator
//                = new SpringSecurityRequestAuthenticator(facade, request, deployment, tokenStore, -1);
//
//        AuthOutcome result = authenticator.authenticate();


        Jwt token = authenticate(request);

        KeycloakAuthenticationToken kctoken = Optional.ofNullable(token)
                .map(KeycloakAuthenticationToken::from)
                .orElseThrow(() -> new BadCredentialsException("Invalid authorization header, see WWW-Authenticate header for details"));
        SecurityContextHolder.getContext().setAuthentication(kctoken);
        return kctoken;
    }


    public Jwt authenticate(HttpServletRequest request) {
        List<String> authHeaders = Collections.list(request.getHeaders("Authorization"));
        if (authHeaders == null || authHeaders.size() == 0) {
            return null;
        }

        String tokenString = null;
        for (String authHeader : authHeaders) {
            String[] split = authHeader.trim().split("\\s+");
            if (split == null || split.length != 2) continue;
            if (!split[0].equalsIgnoreCase("Bearer")) continue;
            tokenString = split[1];
        }

        if (tokenString == null) {
            return null;
        }

        return (authenticateToken(tokenString));
    }

    protected Jwt authenticateToken(String tokenString) {
        log.debug("Verifying access_token");

        try {
            Jwt jwsInput = new NimbusJwtDecoderJwkSupport(jwkSetUrl).decode(tokenString);
            return jwsInput;
        } catch (Exception e) {
            log.error("Failed to parse access_token: " + tokenString, e);
        }

        return null;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("Authentication success using bearer token/basic authentication. Updating SecurityContextHolder to contain: {}", authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

        // Fire event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }

        try {
            chain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }


    /**
     * This filter does not support explicitly enabling session creation.
     *
     * @throws UnsupportedOperationException this filter does not support explicitly enabling session creation.
     */
    @Override
    public final void setAllowSessionCreation(boolean allowSessionCreation) {
        throw new UnsupportedOperationException("This filter does not support explicitly setting a session creation policy");
    }

    /**
     * This filter does not support explicitly setting a continue chain before success policy
     *
     * @throws UnsupportedOperationException this filter does not support explicitly setting a continue chain before success policy
     */
    @Override
    public final void setContinueChainBeforeSuccessfulAuthentication(boolean continueChainBeforeSuccessfulAuthentication) {
        throw new UnsupportedOperationException("This filter does not support explicitly setting a continue chain before success policy");
    }
}

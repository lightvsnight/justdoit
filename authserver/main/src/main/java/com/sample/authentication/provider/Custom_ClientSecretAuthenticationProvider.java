package com.sample.authentication.provider;

import com.sample.authentication.verifier.CodeVerifierAuthenticator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jspecify.annotations.Nullable;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

import java.time.Instant;

public class Custom_ClientSecretAuthenticationProvider implements AuthenticationProvider {
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1";
    private final Log logger = LogFactory.getLog(this.getClass());
    private final RegisteredClientRepository registeredClientRepository;
    private final CodeVerifierAuthenticator codeVerifierAuthenticator;
    private PasswordEncoder passwordEncoder;

    public Custom_ClientSecretAuthenticationProvider(RegisteredClientRepository registeredClientRepository, OAuth2AuthorizationService authorizationService) {
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        this.registeredClientRepository = registeredClientRepository;
        this.codeVerifierAuthenticator = new CodeVerifierAuthenticator(authorizationService);
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2ClientAuthenticationToken clientAuthentication = (OAuth2ClientAuthenticationToken)authentication;
        if (!ClientAuthenticationMethod.CLIENT_SECRET_BASIC.equals(clientAuthentication.getClientAuthenticationMethod()) && !ClientAuthenticationMethod.CLIENT_SECRET_POST.equals(clientAuthentication.getClientAuthenticationMethod())) {
            return null;
        } else {
            String clientId = clientAuthentication.getPrincipal().toString();
            RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
            if (registeredClient == null) {
                throwInvalidClient("client_id");
            }

            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Retrieved registered client");
            }

            if (!registeredClient.getClientAuthenticationMethods().contains(clientAuthentication.getClientAuthenticationMethod())) {
                throwInvalidClient("authentication_method");
            }

            if (clientAuthentication.getCredentials() == null) {
                throwInvalidClient("credentials");
            }

            String clientSecret = clientAuthentication.getCredentials().toString();
            if (!this.passwordEncoder.matches(clientSecret, registeredClient.getClientSecret())) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug(LogMessage.format("Invalid request: client_secret does not match for registered client '%s'", registeredClient.getId()));
                }

                throwInvalidClient("client_secret");
            }

            if (registeredClient.getClientSecretExpiresAt() != null && Instant.now().isAfter(registeredClient.getClientSecretExpiresAt())) {
                throwInvalidClient("client_secret_expires_at");
            }

            if (this.passwordEncoder.upgradeEncoding(registeredClient.getClientSecret())) {
                registeredClient = RegisteredClient.from(registeredClient).clientSecret(this.passwordEncoder.encode(clientSecret)).build();
                this.registeredClientRepository.save(registeredClient);
            }

            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Validated client authentication parameters");
            }

            this.codeVerifierAuthenticator.authenticateIfAvailable(clientAuthentication, registeredClient);
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Authenticated client secret");
            }

            return new OAuth2ClientAuthenticationToken(registeredClient, clientAuthentication.getClientAuthenticationMethod(), clientAuthentication.getCredentials());
        }
    }

    public boolean supports(Class<?> authentication) {
        return OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static void throwInvalidClient(String parameterName) {
        OAuth2Error error = new OAuth2Error("invalid_client", "Client authentication failed: " + parameterName, "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1");
        throw new OAuth2AuthenticationException(error);
    }
}

package com.sample.authentication.verifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CodeVerifierAuthenticator {

    private static final OAuth2TokenType AUTHORIZATION_CODE_TOKEN_TYPE = new OAuth2TokenType("code");
    private final Log logger = LogFactory.getLog(this.getClass());
    private final OAuth2AuthorizationService authorizationService;

    public CodeVerifierAuthenticator(OAuth2AuthorizationService authorizationService) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        this.authorizationService = authorizationService;
    }

    void authenticateRequired(OAuth2ClientAuthenticationToken clientAuthentication, RegisteredClient registeredClient) {
        if (!this.authenticate(clientAuthentication, registeredClient)) {
            throwInvalidGrant("code_verifier");
        }

    }

    public void authenticateIfAvailable(OAuth2ClientAuthenticationToken clientAuthentication, RegisteredClient registeredClient) {
        this.authenticate(clientAuthentication, registeredClient);
    }

    private boolean authenticate(OAuth2ClientAuthenticationToken clientAuthentication, RegisteredClient registeredClient) {
        Map<String, Object> parameters = clientAuthentication.getAdditionalParameters();
        if (!authorizationCodeGrant(parameters)) {
            return false;
        } else {
            OAuth2Authorization authorization = this.authorizationService.findByToken((String)parameters.get("code"), AUTHORIZATION_CODE_TOKEN_TYPE);
            if (authorization == null) {
                throwInvalidGrant("code");
            }

            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Retrieved authorization with authorization code");
            }

            /**
             * 原版的逻辑
             * OAuth2AuthorizationRequest authorizationRequest = (OAuth2AuthorizationRequest)authorization.getAttribute(OAuth2AuthorizationRequest.class.getName());
             */
//            OAuth2AuthorizationRequest authorizationRequest = (OAuth2AuthorizationRequest)authorization.getAttribute(OAuth2AuthorizationRequest.class.getName());

            Map<String,Object> map  = authorization.getAttribute(OAuth2AuthorizationRequest.class.getName());

            String cleanStr = map.get("scopes").toString().replace("[", "").replace("]", "").replace(" ", "");
            Set<String> scopeSet = new HashSet<>(Arrays.asList(cleanStr.split(",")));
            System.out.println(scopeSet); // [openid, profile]（重复元素已去重）


            OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode();
            OAuth2AuthorizationRequest authorizationRequest = builder.clientId(map.get("clientId").toString())
                                          .authorizationUri(map.get("authorizationUri").toString())
                                        .redirectUri(map.get("authorizationUri").toString())
                                        .attributes((Map)map.get("attributes"))
                                        .state(map.get("state").toString())
                                        .scopes(scopeSet)
                                        .additionalParameters((Map)map.get("additionalParameters"))
                                        .build();



            String codeChallenge = (String)authorizationRequest.getAdditionalParameters().get("code_challenge");
            String codeVerifier = (String)parameters.get("code_verifier");
            if (!StringUtils.hasText(codeChallenge)) {
                if (!registeredClient.getClientSettings().isRequireProofKey() && !StringUtils.hasText(codeVerifier)) {
                    if (this.logger.isTraceEnabled()) {
                        this.logger.trace("Did not authenticate code verifier since requireProofKey=false");
                    }

                    return false;
                }

                if (this.logger.isDebugEnabled()) {
                    this.logger.debug(LogMessage.format("Invalid request: code_challenge is required for registered client '%s'", registeredClient.getId()));
                }

                throwInvalidGrant("code_challenge");
            }

            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Validated code verifier parameters");
            }

            String codeChallengeMethod = (String)authorizationRequest.getAdditionalParameters().get("code_challenge_method");
            if (!this.codeVerifierValid(codeVerifier, codeChallenge, codeChallengeMethod)) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug(LogMessage.format("Invalid request: code_verifier is missing or invalid for registered client '%s'", registeredClient.getId()));
                }

                throwInvalidGrant("code_verifier");
            }

            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Authenticated code verifier");
            }

            return true;
        }
    }

    private static boolean authorizationCodeGrant(Map<String, Object> parameters) {
        if (!AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(parameters.get("grant_type"))) {
            return false;
        } else {
            if (!StringUtils.hasText((String)parameters.get("code"))) {
                throwInvalidGrant("code");
            }

            return true;
        }
    }

    private boolean codeVerifierValid(String codeVerifier, String codeChallenge, String codeChallengeMethod) {
        if (!StringUtils.hasText(codeVerifier)) {
            return false;
        } else if ("S256".equals(codeChallengeMethod)) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] digest = md.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
                String encodedVerifier = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
                return encodedVerifier.equals(codeChallenge);
            } catch (NoSuchAlgorithmException var7) {
                throw new OAuth2AuthenticationException("server_error");
            }
        } else {
            return false;
        }
    }

    private static void throwInvalidGrant(String parameterName) {
        OAuth2Error error = new OAuth2Error("invalid_grant", "Client authentication failed: " + parameterName, (String)null);
        throw new OAuth2AuthenticationException(error);
    }
}

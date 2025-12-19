package com.sample.authentication.provider;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
/**
 *
 * @author Li HongKun
 * @since 1.1
 */
public final class OAuth2AuthorizationCodeRequestAuthenticationValidator implements Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> {
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";
    private static final String PKCE_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc7636#section-4.4.1";
    private static final Log LOGGER = LogFactory.getLog(org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationValidator.class);
    static final Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> DEFAULT_AUTHORIZATION_GRANT_TYPE_VALIDATOR = OAuth2AuthorizationCodeRequestAuthenticationValidator::validateAuthorizationGrantType;
    static final Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> DEFAULT_CODE_CHALLENGE_VALIDATOR = OAuth2AuthorizationCodeRequestAuthenticationValidator::validateCodeChallenge;
    static final Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> DEFAULT_PROMPT_VALIDATOR = OAuth2AuthorizationCodeRequestAuthenticationValidator::validatePrompt;
    public static final Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> DEFAULT_REDIRECT_URI_VALIDATOR = OAuth2AuthorizationCodeRequestAuthenticationValidator::validateRedirectUri;
    public static final Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> DEFAULT_SCOPE_VALIDATOR = OAuth2AuthorizationCodeRequestAuthenticationValidator::validateScope;
    private final Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> authenticationValidator;

    public OAuth2AuthorizationCodeRequestAuthenticationValidator() {
        this.authenticationValidator = DEFAULT_REDIRECT_URI_VALIDATOR.andThen(DEFAULT_SCOPE_VALIDATOR);
    }

    public void accept(OAuth2AuthorizationCodeRequestAuthenticationContext authenticationContext) {
        this.authenticationValidator.accept(authenticationContext);
    }

    private static void validateAuthorizationGrantType(OAuth2AuthorizationCodeRequestAuthenticationContext authenticationContext) {
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication = (OAuth2AuthorizationCodeRequestAuthenticationToken)authenticationContext.getAuthentication();
        RegisteredClient registeredClient = authenticationContext.getRegisteredClient();
        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(LogMessage.format("Invalid request: requested grant_type is not allowed for registered client '%s'", registeredClient.getId()));
            }

            throwError("unauthorized_client", "client_id", authorizationCodeRequestAuthentication, registeredClient);
        }

    }

    private static void validateRedirectUri(OAuth2AuthorizationCodeRequestAuthenticationContext authenticationContext) {
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication = (OAuth2AuthorizationCodeRequestAuthenticationToken)authenticationContext.getAuthentication();
        RegisteredClient registeredClient = authenticationContext.getRegisteredClient();
        String requestedRedirectUri = authorizationCodeRequestAuthentication.getRedirectUri();
        if (StringUtils.hasText(requestedRedirectUri)) {
            UriComponents requestedRedirect = null;

            try {
                requestedRedirect = UriComponentsBuilder.fromUriString(requestedRedirectUri).build();
            } catch (Exception var9) {
            }

            if (requestedRedirect == null || requestedRedirect.getFragment() != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(LogMessage.format("Invalid request: redirect_uri is missing or contains a fragment for registered client '%s'", registeredClient.getId()));
                }

                throwError("invalid_request", "redirect_uri", authorizationCodeRequestAuthentication, registeredClient);
            }

            if (!isLoopbackAddress(requestedRedirect.getHost())) {
                if (!registeredClient.getRedirectUris().contains(requestedRedirectUri)) {
                    throwError("invalid_request", "redirect_uri", authorizationCodeRequestAuthentication, registeredClient);
                }
            } else {
                boolean validRedirectUri = false;
                Iterator var6 = registeredClient.getRedirectUris().iterator();

                while(var6.hasNext()) {
                    String registeredRedirectUri = (String)var6.next();
                    UriComponentsBuilder registeredRedirect = UriComponentsBuilder.fromUriString(registeredRedirectUri);
                    registeredRedirect.port(requestedRedirect.getPort());
                    if (registeredRedirect.build().toString().equals(requestedRedirect.toString())) {
                        validRedirectUri = true;
                        break;
                    }
                }

                if (!validRedirectUri) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(LogMessage.format("Invalid request: redirect_uri does not match for registered client '%s'", registeredClient.getId()));
                    }

                    throwError("invalid_request", "redirect_uri", authorizationCodeRequestAuthentication, registeredClient);
                }
            }
        } else if (authorizationCodeRequestAuthentication.getScopes().contains("openid") || registeredClient.getRedirectUris().size() != 1) {
            throwError("invalid_request", "redirect_uri", authorizationCodeRequestAuthentication, registeredClient);
        }

    }

    private static void validateScope(OAuth2AuthorizationCodeRequestAuthenticationContext authenticationContext) {
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication = (OAuth2AuthorizationCodeRequestAuthenticationToken)authenticationContext.getAuthentication();
        RegisteredClient registeredClient = authenticationContext.getRegisteredClient();
        Set<String> requestedScopes = authorizationCodeRequestAuthentication.getScopes();
        Set<String> allowedScopes = registeredClient.getScopes();
        if (!requestedScopes.isEmpty() && !allowedScopes.containsAll(requestedScopes)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(LogMessage.format("Invalid request: requested scope is not allowed for registered client '%s'", registeredClient.getId()));
            }

            throwError("invalid_scope", "scope", authorizationCodeRequestAuthentication, registeredClient);
        }

    }

    private static void validateCodeChallenge(OAuth2AuthorizationCodeRequestAuthenticationContext authenticationContext) {
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication = (OAuth2AuthorizationCodeRequestAuthenticationToken)authenticationContext.getAuthentication();
        RegisteredClient registeredClient = authenticationContext.getRegisteredClient();
        String codeChallenge = (String)authorizationCodeRequestAuthentication.getAdditionalParameters().get("code_challenge");
        if (StringUtils.hasText(codeChallenge)) {
            String codeChallengeMethod = (String)authorizationCodeRequestAuthentication.getAdditionalParameters().get("code_challenge_method");
            if (!StringUtils.hasText(codeChallengeMethod) || !"S256".equals(codeChallengeMethod)) {
                throwError("invalid_request", "code_challenge_method", "https://datatracker.ietf.org/doc/html/rfc7636#section-4.4.1", authorizationCodeRequestAuthentication, registeredClient);
            }
        } else if (registeredClient.getClientSettings().isRequireProofKey()) {
            throwError("invalid_request", "code_challenge", "https://datatracker.ietf.org/doc/html/rfc7636#section-4.4.1", authorizationCodeRequestAuthentication, registeredClient);
        }

    }

    private static void validatePrompt(OAuth2AuthorizationCodeRequestAuthenticationContext authenticationContext) {
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication = (OAuth2AuthorizationCodeRequestAuthenticationToken)authenticationContext.getAuthentication();
        RegisteredClient registeredClient = authenticationContext.getRegisteredClient();
        if (authorizationCodeRequestAuthentication.getScopes().contains("openid")) {
            String prompt = (String)authorizationCodeRequestAuthentication.getAdditionalParameters().get("prompt");
            if (StringUtils.hasText(prompt)) {
                Set<String> promptValues = new HashSet(Arrays.asList(StringUtils.delimitedListToStringArray(prompt, " ")));
                if (promptValues.contains("none") && (promptValues.contains("login") || promptValues.contains("consent") || promptValues.contains("select_account"))) {
                    throwError("invalid_request", "prompt", authorizationCodeRequestAuthentication, registeredClient);
                }
            }
        }

    }

    private static boolean isLoopbackAddress(String host) {
        if (!StringUtils.hasText(host)) {
            return false;
        } else if (!"[0:0:0:0:0:0:0:1]".equals(host) && !"[::1]".equals(host)) {
            String[] ipv4Octets = host.split("\\.");
            if (ipv4Octets.length != 4) {
                return false;
            } else {
                try {
                    int[] address = new int[ipv4Octets.length];

                    for(int i = 0; i < ipv4Octets.length; ++i) {
                        address[i] = Integer.parseInt(ipv4Octets[i]);
                    }

                    return address[0] == 127 && address[1] >= 0 && address[1] <= 255 && address[2] >= 0 && address[2] <= 255 && address[3] >= 1 && address[3] <= 255;
                } catch (NumberFormatException var4) {
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    private static void throwError(String errorCode, String parameterName, OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient) {
        throwError(errorCode, parameterName, "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1", authorizationCodeRequestAuthentication, registeredClient);
    }

    private static void throwError(String errorCode, String parameterName, String errorUri, OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
        throwError(error, parameterName, authorizationCodeRequestAuthentication, registeredClient);
    }

    private static void throwError(OAuth2Error error, String parameterName, OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient) {
        String redirectUri = StringUtils.hasText(authorizationCodeRequestAuthentication.getRedirectUri()) ? authorizationCodeRequestAuthentication.getRedirectUri() : (String)registeredClient.getRedirectUris().iterator().next();
        if (error.getErrorCode().equals("invalid_request") && parameterName.equals("redirect_uri")) {
            redirectUri = null;
        }

        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthenticationResult = new OAuth2AuthorizationCodeRequestAuthenticationToken(authorizationCodeRequestAuthentication.getAuthorizationUri(), authorizationCodeRequestAuthentication.getClientId(), (Authentication)authorizationCodeRequestAuthentication.getPrincipal(), redirectUri, authorizationCodeRequestAuthentication.getState(), authorizationCodeRequestAuthentication.getScopes(), authorizationCodeRequestAuthentication.getAdditionalParameters());
        authorizationCodeRequestAuthenticationResult.setAuthenticated(true);
        throw new OAuth2AuthorizationCodeRequestAuthenticationException(error, authorizationCodeRequestAuthenticationResult);
    }
}


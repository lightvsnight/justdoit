package com.sample.authentication.provider;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.time.Instant;
import java.util.Base64;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
/**
 *
 * @author Li HongKun
 * @since 1.1
 */
final class OAuth2PushedAuthorizationRequestUri {
    private static final String REQUEST_URI_PREFIX = "urn:ietf:params:oauth:request_uri:";
    private static final String REQUEST_URI_DELIMITER = "___";
    private static final StringKeyGenerator DEFAULT_STATE_GENERATOR = new Base64StringKeyGenerator(Base64.getUrlEncoder());
    private String requestUri;
    private String state;
    private Instant expiresAt;

    static OAuth2PushedAuthorizationRequestUri create() {
        return create(Instant.now().plusSeconds(300L));
    }

    static OAuth2PushedAuthorizationRequestUri create(Instant expiresAt) {
        String state = DEFAULT_STATE_GENERATOR.generateKey();
        OAuth2PushedAuthorizationRequestUri pushedAuthorizationRequestUri = new OAuth2PushedAuthorizationRequestUri();
        pushedAuthorizationRequestUri.requestUri = "urn:ietf:params:oauth:request_uri:" + state + "___" + expiresAt.toEpochMilli();
        pushedAuthorizationRequestUri.state = state + "___" + expiresAt.toEpochMilli();
        pushedAuthorizationRequestUri.expiresAt = expiresAt;
        return pushedAuthorizationRequestUri;
    }

    static OAuth2PushedAuthorizationRequestUri parse(String requestUri) {
        int stateStartIndex = "urn:ietf:params:oauth:request_uri:".length();
        int expiresAtStartIndex = requestUri.indexOf("___") + "___".length();
        OAuth2PushedAuthorizationRequestUri pushedAuthorizationRequestUri = new OAuth2PushedAuthorizationRequestUri();
        pushedAuthorizationRequestUri.requestUri = requestUri;
        pushedAuthorizationRequestUri.state = requestUri.substring(stateStartIndex);
        pushedAuthorizationRequestUri.expiresAt = Instant.ofEpochMilli(Long.parseLong(requestUri.substring(expiresAtStartIndex)));
        return pushedAuthorizationRequestUri;
    }

    String getRequestUri() {
        return this.requestUri;
    }

    String getState() {
        return this.state;
    }

    Instant getExpiresAt() {
        return this.expiresAt;
    }

    private OAuth2PushedAuthorizationRequestUri() {
    }
}


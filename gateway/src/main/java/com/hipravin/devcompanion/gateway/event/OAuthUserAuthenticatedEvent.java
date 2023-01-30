package com.hipravin.devcompanion.gateway.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public class OAuthUserAuthenticatedEvent extends ApplicationEvent {
    private final OAuth2AuthenticationToken oauthToken;

    public OAuthUserAuthenticatedEvent(Object source, OAuth2AuthenticationToken token) {
        super(source);
        this.oauthToken = token;
    }

    public OAuth2AuthenticationToken getOauthToken() {
        return oauthToken;
    }
}

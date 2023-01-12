package com.hipravin.devcompanion.gateway.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/session")
public class SessionStateController {
    private static final Logger log = LoggerFactory.getLogger(SessionStateController.class);

    @GetMapping("/current")
    public ResponseEntity<Object> currentSessionInfo(WebSession webSession) {
        logOidcAttributes(webSession);

        return (webSession != null)
                ? ResponseEntity.ok(SessionInfoDto.from(webSession))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/current-mono")
    public Mono<SessionInfoDto> currentSessionInfoMono(WebSession webSession) {
        return (webSession != null)
                ? Mono.just(SessionInfoDto.from(webSession))
                : Mono.empty();
    }

    @GetMapping(path = "/current-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SessionInfoDto> currentSessionInfoSse(WebSession webSession) {
        Duration frequency = Duration.ofSeconds(1);

        return Mono.just(webSession).repeat()
                .delayElements(frequency)
                .map(SessionInfoDto::from);
    }

    private void logOidcAttributes(WebSession webSession) {
        if(webSession != null) {
            Object principal = ((SecurityContextImpl) webSession.getAttribute("SPRING_SECURITY_CONTEXT")).getAuthentication().getPrincipal();
            if(principal instanceof DefaultOidcUser oidcPrincipal) {
//                log.info("OIDC: {}", oidcPrincipal);
                OidcIdToken idToken = oidcPrincipal.getIdToken();
                log.info("OIDC token, exp: {} {}", idToken.getExpiresAt(), idToken.getTokenValue());
            }
        }
    }
}

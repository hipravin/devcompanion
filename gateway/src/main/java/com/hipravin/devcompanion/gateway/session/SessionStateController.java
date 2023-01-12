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
import java.util.Optional;

import static java.util.Optional.ofNullable;

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
        ofNullable(webSession)
                .flatMap(ws -> ofNullable(ws.getAttribute("SPRING_SECURITY_CONTEXT")))
                .filter(sc -> sc instanceof SecurityContextImpl).map(sc -> (SecurityContextImpl) sc)
                .flatMap(sc -> ofNullable(sc.getAuthentication()))
                .flatMap(auth -> ofNullable(auth.getPrincipal()))
                .filter(p -> p instanceof DefaultOidcUser).map(p -> (DefaultOidcUser) p)
                .flatMap(p -> ofNullable(p.getIdToken()))
                .ifPresent(idToken -> {
                    if (log.isDebugEnabled()) {
                        log.debug("OIDC token from session attribute, exp: {} {}", idToken.getExpiresAt(), idToken.getTokenValue());
                    }
                });
    }
}

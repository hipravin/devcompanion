package com.hipravin.devcompanion.gateway.filter;

import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
//this class is from sample.
// first of all it has produced NPE because 'token' was null
//second - it is applied on any request irrespective of whether it's needed or not
// however I've fixed it and kept to see it in action
//and lastly - it's redundant because cookie such cookie is created automatically
//the only difference with standard is in max age
public class CsrfCookieWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		String key = CsrfToken.class.getName();
		Mono<CsrfToken> csrfTokenAttribute = exchange.getAttribute(key);
		Mono<CsrfToken> csrfToken = csrfTokenAttribute != null ? csrfTokenAttribute : Mono.empty();
		return csrfToken.doOnSuccess(token -> {
			if(token != null) {
				ResponseCookie cookie = ResponseCookie.from("X-XSRF-TOKEN", token.getToken()).maxAge(Duration.ofHours(1))
						.httpOnly(false).path("/").sameSite(Cookie.SameSite.LAX.attributeValue()).build();
				exchange.getResponse().getCookies().add("X-XSRF-TOKEN", cookie);
			}
		}).then(chain.filter(exchange));
	}
}

package com.hipravin.devcompanion.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users/")
public class UserController {
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        UserDto userDto = new UserDto(authentication.getName());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/me/info")
    public ResponseEntity<Map<String, String>> getUserInfo(@AuthenticationPrincipal Jwt principal) {
        Map<String, String> claims = new HashMap<>();
        claims.put("user_name", principal.getClaimAsString("preferred_username"));
//        map.put("organization", principal.getClaimAsString("organization"));
        return ResponseEntity.ok(claims);
    }

    @PostMapping("/me/authorities")
    public ResponseEntity<Map<String,Object>> getPrincipalInfo(JwtAuthenticationToken principal) {

        Collection<String> authorities = principal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String,Object> info = new HashMap<>();
        info.put("name", principal.getName());
        info.put("authorities", authorities);
        info.put("tokenAttributes", principal.getTokenAttributes());

        return ResponseEntity.ok(info);
    }
}

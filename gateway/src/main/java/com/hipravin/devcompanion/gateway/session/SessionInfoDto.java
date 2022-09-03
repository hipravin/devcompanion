package com.hipravin.devcompanion.gateway.session;

import org.springframework.web.server.WebSession;

import java.time.Instant;

public class SessionInfoDto {
    private String id;
    private Instant creationTime;
    private Instant lastAccessTime;
    private Long sessionMaxIdleTimeMillis;

    public SessionInfoDto() {
    }

    public SessionInfoDto(String id, Instant creationTime, Instant lastAccessTime, Long sessionMaxIdleTimeMillis) {
        this.id = id;
        this.creationTime = creationTime;
        this.lastAccessTime = lastAccessTime;
        this.sessionMaxIdleTimeMillis = sessionMaxIdleTimeMillis;
    }

    public static SessionInfoDto from(WebSession webSession) {
        return new SessionInfoDto(webSession.getId(),
                webSession.getCreationTime(),
                webSession.getLastAccessTime(),
                sessionIdleTimeMillisOrNull(webSession));
    }

    private static Long sessionIdleTimeMillisOrNull(WebSession webSession) {
        return (webSession != null) && (webSession.getMaxIdleTime() != null)
                ? webSession.getMaxIdleTime().toMillis()
                : null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public Instant getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Instant lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Long getSessionMaxIdleTimeMillis() {
        return sessionMaxIdleTimeMillis;
    }

    public void setSessionMaxIdleTimeMillis(Long sessionMaxIdleTimeMillis) {
        this.sessionMaxIdleTimeMillis = sessionMaxIdleTimeMillis;
    }
}

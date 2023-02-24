package com.hipravin.devcompanion.gateway.playground;

import java.time.OffsetDateTime;

public class PlaygroundDto {
    public OffsetDateTime requestProcessingStartTime;
    public OffsetDateTime requestProcessingEndTime;
    public String message;

    public PlaygroundDto() {
    }

    public PlaygroundDto(OffsetDateTime requestProcessingStartTime,
                         OffsetDateTime requestProcessingEndTime,
                         String message) {
        this.requestProcessingStartTime = requestProcessingStartTime;
        this.requestProcessingEndTime = requestProcessingEndTime;
        this.message = message;
    }

    public OffsetDateTime getRequestProcessingStartTime() {
        return requestProcessingStartTime;
    }

    public void setRequestProcessingStartTime(OffsetDateTime requestProcessingStartTime) {
        this.requestProcessingStartTime = requestProcessingStartTime;
    }

    public OffsetDateTime getRequestProcessingEndTime() {
        return requestProcessingEndTime;
    }

    public void setRequestProcessingEndTime(OffsetDateTime requestProcessingEndTime) {
        this.requestProcessingEndTime = requestProcessingEndTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

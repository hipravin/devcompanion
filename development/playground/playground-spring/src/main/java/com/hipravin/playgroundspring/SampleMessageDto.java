package com.hipravin.playgroundspring;

import java.util.UUID;

public class SampleMessageDto {
    private UUID id;
    private String message;

    public SampleMessageDto() {
    }

    public SampleMessageDto(UUID id, String message) {
        this.id = id;
        this.message = message;
    }

    public static SampleMessageDto createWithRandomId(String message) {
        return new SampleMessageDto(UUID.randomUUID(), message);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SampleMessageDto{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}

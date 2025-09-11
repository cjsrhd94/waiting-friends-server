package com.example.waitingreservationservice.common.util;

import lombok.Getter;

@Getter
public class RawMessage {
    private String eventType;
    private String payload;

    public RawMessage(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
    }
}
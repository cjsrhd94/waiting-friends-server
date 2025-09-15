package com.example.waitingkafka.util;

import lombok.Getter;

@Getter
public class Event {
    private String type;
    private String payload;

    public Event(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }
}
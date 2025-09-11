package com.example.waitingmessage.util;

public interface EventHandler<T> {
    String getEventType();
    void handle(T event);
}

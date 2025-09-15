package com.example.waitingkafka.util;

public interface EventHandler<T> {
    String eventType();
    void handle(T event);
}

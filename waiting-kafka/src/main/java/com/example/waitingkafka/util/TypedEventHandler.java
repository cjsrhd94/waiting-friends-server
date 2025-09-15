package com.example.waitingkafka.util;

public interface TypedEventHandler<T> extends EventHandler<T> {
    Class<T> getDtoClass();

    default void handleDto(Object dto) {
        handle(getDtoClass().cast(dto));
    }
}

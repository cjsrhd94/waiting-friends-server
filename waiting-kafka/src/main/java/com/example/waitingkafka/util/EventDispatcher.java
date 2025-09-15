package com.example.waitingkafka.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventDispatcher {
    private final Map<String, EventHandler<?>> handlers = new HashMap<>();
    private final JsonConverter jsonConverter;

    public EventDispatcher(List<EventHandler<?>> handlerList, JsonConverter jsonConverter) {
        for (EventHandler<?> handler : handlerList) {
            handlers.put(handler.eventType(), handler);
        }
        this.jsonConverter = jsonConverter;
    }

    public void dispatch(Event event) {
        EventHandler<?> handler = handlers.get(event.getType());
        if (handler == null) {
            throw new RuntimeException("등록되지 않은 이벤트 타입: " + event.getType());
        }

        if (handler instanceof TypedEventHandler<?> typedHandler) {
            Object dto = jsonConverter.fromJson(event.getPayload(), typedHandler.getDtoClass());
            typedHandler.handleDto(dto);
        }
    }
}

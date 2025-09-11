package com.example.waitingmessage.util;

import com.example.waitingmessage.event.RawMessage;
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
            handlers.put(handler.getEventType(), handler);
        }
        this.jsonConverter = jsonConverter;
    }

    public void dispatch(RawMessage rawMessage) {
        EventHandler<?> handler = handlers.get(rawMessage.getEventType());
        if (handler == null) {
            throw new RuntimeException("등록되지 않은 이벤트 타입: " + rawMessage.getEventType());
        }

        if (handler instanceof TypedEventHandler<?> typedHandler) {
            Object dto = jsonConverter.fromJson(rawMessage.getPayload(), typedHandler.getDtoClass());
            typedHandler.handleDto(dto);
        }
    }
}

package com.example.waitingreservationservice.common.util;

public enum CacheKey {
    SPOT("spot::");

    private final String key;

    CacheKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

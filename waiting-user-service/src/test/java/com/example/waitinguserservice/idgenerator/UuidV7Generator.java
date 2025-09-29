package com.example.waitinguserservice.idgenerator;

import java.security.SecureRandom;
import java.util.UUID;

public class UuidV7Generator {
    
    private static final SecureRandom RANDOM = new SecureRandom();
    
    public static UUID generate() {
        long timestamp = System.currentTimeMillis();
        long mostSigBits = (timestamp << 16) | (RANDOM.nextInt(0x10000));
        mostSigBits = (mostSigBits & 0x0FFFFFFFFFFFFFFFL) | 0x7000000000000000L;
        
        long leastSigBits = RANDOM.nextLong();
        leastSigBits = (leastSigBits & 0x3FFFFFFFFFFFFFFFL) | 0x8000000000000000L;
        
        return new UUID(mostSigBits, leastSigBits);
    }
}

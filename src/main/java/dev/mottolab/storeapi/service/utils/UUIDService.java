package dev.mottolab.storeapi.service.utils;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public class UUIDService {
    public static UUID generateUUIDV7(){
        return UuidCreator.getTimeOrderedEpoch();
    }

    public static long parseTimestampUUIDV7(String uuid){
        // ChatGPT helped to me :')
        // Parse UUID
        UUID uuidV7 = UUID.fromString(uuid);
        // Extracting the most significant bits
        long mostSignificantBits = uuidV7.getMostSignificantBits();
        // Extract timestamp
        return (mostSignificantBits >> 80) & 0xFFFFFFFFFFFFL;
    }
}

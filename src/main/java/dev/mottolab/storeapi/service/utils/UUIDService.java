package dev.mottolab.storeapi.service.utils;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public class UUIDService {
    public static UUID generateUUIDV7(){
        return UuidCreator.getTimeOrderedEpoch();
    }

    public static long parseTimestampUUIDV7(UUID uid){
        // ChatGPT helped to me :')

        // Extracting the most significant bits
        long mostSignificantBits = uid.getMostSignificantBits();
        // Extract timestamp
        return (mostSignificantBits >> 80) & 0xFFFFFFFFFFFFL;
    }
}

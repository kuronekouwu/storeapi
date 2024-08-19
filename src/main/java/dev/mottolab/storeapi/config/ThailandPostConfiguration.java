package dev.mottolab.storeapi.config;

import dev.mottolab.storeapi.provider.thailandpost.ThailandPostProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThailandPostConfiguration {
    @Value("${config.tracking.thailandpost.token}")
    private String token;

    @Bean
    public ThailandPostProvider thailandPostProvider() {
        return new ThailandPostProvider(token);
    }
}

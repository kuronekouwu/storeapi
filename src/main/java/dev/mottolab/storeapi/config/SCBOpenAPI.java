package dev.mottolab.storeapi.config;

import dev.mottolab.storeapi.provider.scbopenapi.SCBAPIProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SCBOpenAPI {
    @Value("${config.payment.scb.apiUrl}")
    private String apiUrl;
    @Value("${config.payment.scb.clientId}")
    private String clientId;
    @Value("${config.payment.scb.clientSecret}")
    private String clientSecret;
    @Value("${config.payment.scb.merchantId}")
    private String merchantId;

    @Bean
    public SCBAPIProvider scbAPIProvider() {
        return new SCBAPIProvider(
            this.apiUrl,
            this.clientId,
            this.clientSecret,
            this.merchantId
        );
    }
}

package dev.mottolab.storeapi.config;

import dev.mottolab.storeapi.provider.chillpay.ChillpayProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChillpayConfiguration {
    @Value("${config.payment.chillpay.apiUrl}")
    private String apiUrl;
    @Value("${config.payment.chillpay.merchantId}")
    private String merchantId;
    @Value("${config.payment.chillpay.apiKey}")
    private String apiKey;
    @Value("${config.payment.chillpay.md5}")
    private String md5Key;

    @Bean
    public ChillpayProvider chillpayProvider() {
        return new ChillpayProvider(apiUrl, merchantId, apiKey, md5Key);
    }
}

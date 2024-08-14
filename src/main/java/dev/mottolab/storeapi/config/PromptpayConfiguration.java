package dev.mottolab.storeapi.config;

import dev.mottolab.storeapi.provider.promptpay.PromptpayProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromptpayConfiguration {
    @Value("${config.payment.promptpay.type}")
    private String type;
    @Value("${config.payment.promptpay.value}")
    private String value;
    
    @Bean
    public PromptpayProvider initPromptpayProvider(){
        return new PromptpayProvider(this.type, this.value);
    }
}

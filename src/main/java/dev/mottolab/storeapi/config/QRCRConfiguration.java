package dev.mottolab.storeapi.config;

import dev.mottolab.storeapi.provider.rdcw.qrcr.QRCRProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QRCRConfiguration {
    @Bean
    public QRCRProvider qrcrProvider() {
        return new QRCRProvider();
    }
}

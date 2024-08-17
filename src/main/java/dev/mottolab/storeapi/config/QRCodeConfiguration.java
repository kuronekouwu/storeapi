package dev.mottolab.storeapi.config;

import dev.mottolab.storeapi.provider.qrcode.QRCodeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QRCodeConfiguration {
    @Bean
    public QRCodeProvider qrCodeProvider() {
        return new QRCodeProvider();
    }
}

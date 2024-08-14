package dev.mottolab.storeapi.config;

import dev.mottolab.storeapi.provider.rdcw.slipverify.SlipverifyProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RDCWSlipVerifyConfiguration {
    @Value("${config.payment.slip.apiUrl}")
    private String apiUrl;
    @Value("${config.payment.slip.clientId}")
    private String clientId;
    @Value("${config.payment.slip.clientSecret}")
    private String clientSecret;
    @Value("${config.payment.slip.postpaid}")
    private Integer postpaid;

    @Bean
    public SlipverifyProvider slipverifyProvider() {
        return new SlipverifyProvider(
                this.apiUrl,
                this.clientId,
                this.clientSecret,
                this.postpaid
        );
    }
}

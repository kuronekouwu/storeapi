package dev.mottolab.storeapi.config;

import dev.mottolab.storeapi.provider.truemoney.voucher.TruemoneyVoucherProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TmnVoucherConfiguration {
    @Value("${config.payment.tmn_voucher.mobile}")
    private String paymentMobile;
    @Value("${config.payment.tmn_voucher.rate}")
    private String rate;

    @Bean
    public TruemoneyVoucherProvider initTruemoneyVoucherProvider(){
        return new TruemoneyVoucherProvider(this.paymentMobile, Float.parseFloat(this.rate));
    }
}

package dev.mottolab.storeapi.provider.promptpay;

import dev.mottolab.storeapi.provider.promptpay.lib.TLV;
import dev.mottolab.storeapi.provider.promptpay.utils.CRC16;
import dev.mottolab.storeapi.provider.promptpay.utils.Encoder;

import java.util.ArrayList;
import java.util.List;

public class PromptpayProvider {
    private final String mobile;

    public PromptpayProvider(String mobile) {
        System.out.println(mobile);
        this.mobile = mobile;
    }

    public String generateQRCodeWithMSISDN(Double amount){
        String target = this.mobile;
        if(target.startsWith("0")){
            target = "66"+ target.substring(1);
        }
        target = String.format("%1$13s", target).replace(' ', '0');
        if (target.length() > 13) {
            target = target.substring(target.length() - 13);
        }
        List<TLV> tag29 = new ArrayList<>();
        tag29.add(new TLV("00", "A000000677010111"));
        tag29.add(new TLV(PromptpayProxyType.MSISDN.getValue(), target));

        List<TLV> tag = new ArrayList<>();
        tag.add(new TLV("00", "01"));
        tag.add(new TLV("01", amount <= 0 ? "11" : "12"));
        tag.add(new TLV("29", Encoder.encode(tag29)));
        tag.add(new TLV("53", "764"));
        tag.add(new TLV("58", "TH"));

        if(amount > 0){
            tag.add(new TLV("54", String.format("%.2f", (amount*100)/100)));
        }

        // Create to payload
        String payload = Encoder.encode(tag) + "6304";
        return payload + String.format("%04X", CRC16.crc16xmodem(payload, 0xffff));
    }
}

package dev.mottolab.storeapi.provider.rdcw.slipverify.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlipVerifyResponse {
    public Integer code = 0;
    public String message = "Success";
    public SlipVerifyResponse.Data data;
    public boolean valid;
    public boolean isCached;
    public String discriminator;

    @Getter
    public static class Data {
        public String language;
        public String transRef;
        public String sendingBank;
        public String receivingBank;
        public String transDate;
        public String transTime;
        public AccountInformation sender;
        public AccountInformation receiver;
        public Double amount;
        public Double paidLocalAmount;
        public String paidLocalCurrency;
        public String countryCode;
        public int transFeeAmount;
        public String ref1;
        public String ref2;
        public String ref3;
        public String toMerchantId;
    }

    @Getter
    public static class AccountInformation {
        public String displayName;
        public String name;
        public AccountData proxy;
        public AccountData account;
    }

    @Getter
    public static class AccountData {
        public String type;
        public String  value;
    }
}

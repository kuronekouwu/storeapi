package dev.mottolab.storeapi.provider.scbopenapi.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromptpayCreateResult {
    public PromptpayCreateResult.Data data;

    @Getter
    @Setter
    public static class Data {
        public String qrRawData;
        public String qrImage;
    }
}

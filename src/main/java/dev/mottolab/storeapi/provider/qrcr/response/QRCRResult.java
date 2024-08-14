package dev.mottolab.storeapi.provider.qrcr.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QRCRResult {
    private Integer code;
    private String detail;
    private QRCRData[] data;
    private String[] step;

    @Getter
    public static class QRCRData {
        private String type;
        private String value;
    }
}

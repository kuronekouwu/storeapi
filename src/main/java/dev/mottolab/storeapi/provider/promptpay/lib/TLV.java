package dev.mottolab.storeapi.provider.promptpay.lib;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TLV {
    private String tagId;
    private Integer length;
    private String value;

    public TLV(String tagId, String value) {
        this.tagId = tagId;
        this.length = value.length();
        this.value = value;
    }
}

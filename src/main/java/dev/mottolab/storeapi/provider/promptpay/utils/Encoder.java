package dev.mottolab.storeapi.provider.promptpay.utils;

import dev.mottolab.storeapi.provider.promptpay.lib.TLV;

import java.util.List;

public class Encoder {
    public static String encode(List<TLV> tag) {
        StringBuilder builder = new StringBuilder();
        for(TLV tlv : tag){
            builder.append(tlv.getTagId());
            builder.append(String.format("%02d", tlv.getLength()));
            builder.append(tlv.getValue());
        }

        return builder.toString();
    }
}

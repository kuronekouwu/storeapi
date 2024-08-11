package dev.mottolab.storeapi.provider.promptpay;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PromptpayProxyType {
    MSISDN("01"),
    NATID("02"),
    EWALLETID("03");

    @Getter
    private final String value;
}

package dev.mottolab.storeapi.provider.rdcw.slipverify.exception;

import lombok.Getter;

public class SlipVerifyError extends Throwable {
    @Getter
    private Integer code;

    public SlipVerifyError(Integer code, String message) {
        super(message);
        this.code = code;
    }
}

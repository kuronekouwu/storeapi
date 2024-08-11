package dev.mottolab.storeapi.provider.truemoney.voucher.excpetion;

import dev.mottolab.storeapi.provider.truemoney.voucher.response.TmnVoucherStatus;
import lombok.Getter;

@Getter
public class TmnVoucherError extends Throwable {
    public TmnVoucherStatus status;

    public TmnVoucherError(TmnVoucherStatus status, String message) {
        super(message);
        this.status = status;
    }
}

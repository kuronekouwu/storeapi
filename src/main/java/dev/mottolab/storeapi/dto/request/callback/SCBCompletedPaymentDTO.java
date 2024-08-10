package dev.mottolab.storeapi.dto.request.callback;

import java.util.Date;

public record SCBCompletedPaymentDTO(
        String payeeProxyId,
        String payeeProxyType,
        String payeeAccountNumber,
        String payeeName,
        String payerProxyId,
        String payerProxyType,
        String payerAccountNumber,
        String payerName,
        String sendingBankCode,
        String receivingBankCode,
        String amount,
        String channelCode,
        String transactionId,
        Date transactionDateandTime,
        String billPaymentRef1,
        String billPaymentRef2,
        String billPaymentRef3,
        String currencyCode,
        String transactionType
) {
}

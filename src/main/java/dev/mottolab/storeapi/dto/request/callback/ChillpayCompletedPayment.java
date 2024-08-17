package dev.mottolab.storeapi.dto.request.callback;

public record ChillpayCompletedPayment(
        int TransactionId,
        int Amount,
        String OrderNo,
        String CustomerId,
        String BankCode,
        String PaymentDate,
        int PaymentStatus,
        String BankRefCode,
        String CurrentDate,
        String CurrentTime,
        String PaymentDescription,
        String CreditCardToken,
        String Currency,
        String CustomerName,
        String CheckSum
) {
    public String toChecksumString(){
        return TransactionId + String.valueOf(Amount) + OrderNo + CustomerId + BankCode + PaymentDate + String.valueOf(PaymentStatus) +
                BankRefCode + CurrentDate + CurrentTime + PaymentDescription + CreditCardToken + Currency +
                CustomerName;
    }
}

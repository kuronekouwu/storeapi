package dev.mottolab.storeapi.dto.request.callback;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChillpayCompletedPayment {
    public int TransactionId;
    public int Amount;
    public String OrderNo;
    public String CustomerId;
    public String BankCode;
    public String PaymentDate;
    public int PaymentStatus;
    public String BankRefCode;
    public String CurrentDate;
    public String CurrentTime;
    public String PaymentDescription;
    public String CreditCardToken;
    public String Currency;
    public String CustomerName;
    public String CheckSum;

    @Override
    public String toString() {
        return "ChillpayCompletedPayment{" +
                "TransactionId=" + TransactionId +
                ", Amount=" + Amount +
                ", OrderNo='" + OrderNo + '\'' +
                ", CustomerId='" + CustomerId + '\'' +
                ", BankCode='" + BankCode + '\'' +
                ", PaymentDate='" + PaymentDate + '\'' +
                ", PaymentStatus=" + PaymentStatus +
                ", BankRefCode='" + BankRefCode + '\'' +
                ", CurrentDate='" + CurrentDate + '\'' +
                ", CurrentTime='" + CurrentTime + '\'' +
                ", PaymentDescription='" + PaymentDescription + '\'' +
                ", CreditCardToken='" + CreditCardToken + '\'' +
                ", Currency='" + Currency + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                ", CheckSum='" + CheckSum + '\'' +
                '}';
    }

    public String toChecksumString(){
        return TransactionId + String.valueOf(Amount) + OrderNo + CustomerId + BankCode + PaymentDate + String.valueOf(PaymentStatus) +
                BankRefCode + CurrentDate + CurrentTime + PaymentDescription + CreditCardToken + Currency +
                CustomerName;
    }
}

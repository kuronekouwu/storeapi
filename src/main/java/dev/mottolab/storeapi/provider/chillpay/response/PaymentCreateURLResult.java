package dev.mottolab.storeapi.provider.chillpay.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCreateURLResult {
    public int Status;
    public int Code;
    public String Message;
    public int Amount;
    public String OrderNo;
    public String CustomerId;
    public String ReturnUrl;
    public String PaymentUrl;
    public String IpAddress;
    public String Token;
    public int TransactionId;
    public String ChannelCode;
    public String CreatedDate;
    public String ExpiredDate;
}

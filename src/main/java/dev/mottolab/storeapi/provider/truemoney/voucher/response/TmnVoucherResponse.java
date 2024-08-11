package dev.mottolab.storeapi.provider.truemoney.voucher.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class TmnVoucherResponse {
    public TmnVoucherResponse.Status status;
    public Data data;

    public static class Status{
        public String message;
        public TmnVoucherStatus code;
    }

    @Getter
    public static class Data{
        public Voucher voucher;
        @JsonProperty("owner_profile")
        public OwnerProfile ownerProfile;
        // Some if null
        @JsonProperty("redeemer_profile")
        public RedeemerProfile redeemerProfile;
        @JsonProperty("my_ticket")
        public Ticket myTicket;
        public ArrayList<Ticket> tickets;
    }

    @Getter
    public static class OwnerProfile{
        public String full_name;
    }

    @Getter
    public static class RedeemerProfile{
        public String mobile_number;
    }

    @Getter
    public static class Ticket{
        public String mobile;
        @JsonProperty("update_date")
        public long updateDate;
        @JsonProperty("amount_baht")
        public String amountBaht;
        @JsonProperty("full_name")
        public String fullName;
        @JsonProperty("profile_pic")
        public String profileImage;
    }

    @Getter
    public static class Voucher{
        @JsonProperty("voucher_id")
        public String voucherId;
        @JsonProperty("amount_baht")
        public String amountBaht;
        @JsonProperty("redeemed_amount_baht")
        public String redeemedAmountBaht;
        public int member;
        public String status;
        public String link;
        public String detail;
        @JsonProperty("expire_date")
        public long expireDate;
        public String type;
        public int redeemed;
        public int available;
    }
}

package dev.mottolab.storeapi.dto.request.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ThailandPostCallback {
    @JsonProperty("track_datetime")
    public String trackDatetime;
    List<ThailandPostCallbackItem> items = new ArrayList<>();

    @Getter
    @Setter
    public static class ThailandPostCallbackItem{
        public String barcode;
        public String status;
        @JsonProperty("status_description")
        public String statusDescription;
        @JsonProperty("status_date")
        public String statusDate;
        public String statusDetail;
        public String location;
        public String postcode;
        @JsonProperty("delivery_status")
        public String deliveryStatus;
        @JsonProperty("delivery_description")
        public String deliveryDescription;
        @JsonProperty("delivery_datetime")
        public String deliveryDatetime;
        @JsonProperty("receiver_name")
        public String receiverName;
        public String signature;
        @JsonProperty("delivery_officer_name")
        public String deliveryOfficerName;
        @JsonProperty("delivery_officer_tel")
        public String deliveryOfficerTel;
        @JsonProperty("office_name")
        public String officeName;
        @JsonProperty("office_tel")
        public String officeTel;
        @JsonProperty("call_center_tel")
        public String callCenterTel;
    }
}

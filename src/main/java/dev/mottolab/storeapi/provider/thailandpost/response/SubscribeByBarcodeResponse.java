package dev.mottolab.storeapi.provider.thailandpost.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubscribeByBarcodeResponse {
    public SubscribeByBarcodeResponse.Response response;
    public String message;
    public Boolean status;

    public static class Item {
        public String barcode;
        public Boolean status;
    }

    public static class Response{
        public List<SubscribeByBarcodeResponse.Item> items;
        @JsonProperty("track_count")
        public SubscribeByBarcodeResponse.TrackCount trackCount;
    }

    public static class TrackCount {
        @JsonProperty("track_date")
        public String trackDate;
        @JsonProperty("count_number")
        public Integer countNumber;
        @JsonProperty("track_count_limit")
        public Integer trackCountLimit;
    }
}

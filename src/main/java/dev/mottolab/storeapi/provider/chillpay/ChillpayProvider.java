package dev.mottolab.storeapi.provider.chillpay;

import com.google.gson.Gson;
import dev.mottolab.storeapi.provider.chillpay.exception.ChillpayCreatePaymentFail;
import dev.mottolab.storeapi.provider.chillpay.response.PaymentCreateURLResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Slf4j
public class ChillpayProvider {
    private String apiUrl = "https://appsrv.chillpay.co";
    private String apiKey;
    private String merchantId;
    private String md5Key;
    // HTTP Client
    private OkHttpClient httpClient = new OkHttpClient();
    // Parser
    private Gson gson = new Gson();
    // Checksum
    private String[] checksumPaymentCreateField = {
        "MerchantCode",
        "OrderNo",
        "CustomerId",
        "Amount",
        "ChannelCode",
        "Currency",
        "RouteNo",
        "IPAddress",
        "ApiKey"
    };


    public ChillpayProvider(String apiUrl, String merchantId, String apiKey, String md5Key) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.merchantId = merchantId;
        this.md5Key = md5Key;
    }

    public PaymentCreateURLResult createPaymentURL(CreatePaymentURL payload) throws ChillpayCreatePaymentFail {
        // Create object
        HashMap hashMap = new HashMap<>();
        hashMap.put("MerchantCode", this.merchantId);
        hashMap.put("OrderNo", payload.orderId);
        hashMap.put("CustomerId", payload.customerId);
        hashMap.put("Amount", (int)(payload.amount * 100));
        hashMap.put("ChannelCode", payload.channelCode.value);
        hashMap.put("Currency", payload.currency.value);
        hashMap.put("RouteNo", payload.routeNo);
        hashMap.put("IPAddress", payload.ipAddress);
        hashMap.put("ApiKey", this.apiKey);

        // Checksum payload
        String checksum = this.checksumMd5ByHashMap(hashMap);
        hashMap.put("CheckSum", checksum);

        RequestBody formBody = this.convertFromMapToRequestBody(hashMap);

        // Create request builder
        Request req = new Request.Builder()
                .url(this.apiUrl + "/api/v2/Payment")
                .method("POST", formBody)
                .build();

        try {
            // Do request it
            Response response = this.httpClient.newCall(req).execute();

            // Parse body
            String body = response.body().string();
            log.info("Response body from ChillPay Server: {}", body);
            PaymentCreateURLResult result = gson.fromJson(body, PaymentCreateURLResult.class);
            if(result.Status != 0){
                throw new ChillpayCreatePaymentFail(result.Code, result.Message);
            }

            return result;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public Boolean verifyChecksum(String raw, String checksum) {
        return this.checksumMd5ByString(raw).equals(checksum);
    }

    private RequestBody convertFromMapToRequestBody(HashMap hashMap) {
        FormBody.Builder builder = new FormBody.Builder();
        for(Object key : hashMap.keySet()) {
            builder.add(key.toString(), hashMap.get(key).toString());
        }

        return builder.build();
    }

    private String checksumMd5ByHashMap(HashMap body){
        StringBuilder checksum = new StringBuilder();
        for(Object key : checksumPaymentCreateField){
            checksum.append(body.get(key).toString());
        }

        return checksumMd5ByString(checksum.toString());
    }

    private String checksumMd5ByString(String body){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((body + this.md5Key).getBytes());
            return this.toHexString(md.digest());
        }catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    @Getter
    @Setter
    public static class CreatePaymentURL {
        private Double amount;
        private String orderId;
        private String customerId;
        private PaymentMethod channelCode;
        private PaymentCurrency currency = PaymentCurrency.THB;
        private String routeNo = "1";
        private String ipAddress;
    }


    @Getter
    @RequiredArgsConstructor
    public enum PaymentMethod {
        INTERNETBANK_BAY("internetbank_bay"),
        INTERNETBANK_BBL("internetbank_bbl"),
        INTERNETBANK_SCB("internetbank_scb"),
        INTERNETBANK_TTB("internetbank_ttb"),
        PAYPLUS_KBANK("payplus_kbank"),
        MOBILEBANK_SCB("mobilebank_scb"),
        MOBILEBANK_BAY("mobilebank_bay"),
        MOBILEBANK_BBL("mobilebank_bl"),
        MOBILEBANK_KTB("mobilebank_ktb"),
        CREDIT_CARD("creditcard"),
        ALIPAY("epayment_alipay"),
        WECHATPAY("epayment_wechatpay"),
        TRUEMONEY("epayment_truemoney"),
        SHOPEEPAY("epayment_shopeepay"),
        PAOTANG("epayment_paotang"),
        PROMPTPAY("bank_qrcode");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PaymentCurrency {
        THB("764"),
        USD("840"),
        EUR("978"),
        JPY("392"),
        GBP("826"),
        AUD("036"),
        NZD("554"),
        HKD("344"),
        SGD("702"),
        CHF("756"),
        MYR("458"),
        CNY("156");

        private final String value;
    }
}

package dev.mottolab.storeapi.provider.scbopenapi;

import dev.mottolab.storeapi.provider.scbopenapi.response.OAuth2Token;
import dev.mottolab.storeapi.provider.scbopenapi.response.PaymentPromptpayCreateResult;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.UUID;

public class SCBAPIProvider {
    private final String apiUrl;
    private final String clientId;
    private final String clientSecret;
    private final String merchantId;
    // OAuth2 Config
    private String accessToken;
    private String tokenType;
    private Integer expiredIn;
    // HTTP Client
    private OkHttpClient httpClient = new OkHttpClient();
    // Parser
    private Gson gson = new Gson();

    public SCBAPIProvider(
            String apiUrl,
            String clientId,
            String clientSecret,
            String merchantId
    ) {
        this.apiUrl = apiUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.merchantId = merchantId;
        // Init token
        this.initToken();
    }

    private void initToken(){
        if(!this.generateToken()){
            System.out.println("[Warning] Can't get access token. Please check API URL or Credential and try again.");
        }
    }

    private Boolean isExpired(){
        return System.currentTimeMillis() > this.expiredIn;
    }

    public Boolean generateToken() {
        // Create object
        HashMap hashMap = new HashMap<>();
        hashMap.put("applicationKey", this.clientId);
        hashMap.put("applicationSecret", this.clientSecret);

        // Create request body
        RequestBody body = RequestBody.create(
                gson.toJson(gson.toJsonTree(hashMap)),
                MediaType.parse("application/json")
        );

        // Create request builder
        Request req = new Request.Builder()
                .url(this.apiUrl + "/partners/sandbox/v1/oauth/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("resourceOwnerId", this.clientId)
                .addHeader("requestUId", UUID.randomUUID().toString())
                .addHeader("accept-language", "EN")
                .build();

        try {
            // Do request it
            Response response = this.httpClient.newCall(req).execute();

            if(response.isSuccessful()) {
                // Parse body
                if(response.body() != null){
                    OAuth2Token parsed = gson.fromJson(response.body().string(), OAuth2Token.class);
                    this.accessToken = parsed.getData().getAccessToken();
                    this.tokenType = parsed.getData().getTokenType();
                    this.expiredIn = parsed.getData().getExpiresAt() * 1000;

                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public PaymentPromptpayCreateResult generatePromptpayQrCode(GeneratePromptpayQrCode payload){
        if(this.isExpired()){
            this.initToken();
        }

        // Create object
        HashMap hashMap = new HashMap<>();
        hashMap.put("qrType", "PP");
        hashMap.put("ppType", "BILLERID");
        hashMap.put("ppId", this.merchantId);
        hashMap.put("amount", payload.getAmount().toString());
        hashMap.put("ref1", payload.getRef1());
        hashMap.put("ref2", payload.getRef2());
        hashMap.put("ref3", payload.getRef3());

        // Create request body
        RequestBody body = RequestBody.create(
                gson.toJson(gson.toJsonTree(hashMap)),
                MediaType.parse("application/json")
        );

        // Create request builder
        Request req = new Request.Builder()
                .url(this.apiUrl + "/partners/sandbox/v1/payment/qrcode/create")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("resourceOwnerId", this.clientId)
                .addHeader("Authorization", this.tokenType + " " + this.accessToken)
                .addHeader("requestUId", UUID.randomUUID().toString())
                .addHeader("accept-language", "EN")
                .build();

        try {
            // Do request it
            Response response = this.httpClient.newCall(req).execute();

            if(response.isSuccessful()) {
                // Parse body
                if(response.body() != null){
                    return gson.fromJson(response.body().string(), PaymentPromptpayCreateResult.class);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Getter
    @Setter
    public static class GeneratePromptpayQrCode {
        private Double amount;
        private String ref1;
        private String ref2;
        private String ref3 = "SCB";
    }
}

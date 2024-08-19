package dev.mottolab.storeapi.provider.thailandpost;

import com.google.gson.Gson;
import dev.mottolab.storeapi.provider.scbopenapi.response.AuthenticateToken;
import dev.mottolab.storeapi.provider.thailandpost.response.SubscribeByBarcodeResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
public class ThailandPostProvider {
    private final String apiUrl = "https://trackwebhook.thailandpost.co.th";
    private final String token;
    // Token
    private String accessToken;
    private Long expiredIn;
    // HTTP Client
    private final OkHttpClient httpClient = new OkHttpClient();
    // Parser
    private final Gson gson = new Gson();

    public ThailandPostProvider(String token) {
        this.token = token;
    }

    private Boolean isExpired(){
        if(this.expiredIn != null){
            return System.currentTimeMillis() > this.expiredIn;
        }

        return true;
    }

    public Boolean subscribeByBarcode(String barcode) {
        // Check if token has expired
        if(isExpired()){
            initToken();
        }

        // Create array barcode
        ArrayList<String> list = new ArrayList<>();
        list.add(barcode);

        // Create object
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("status", "all");
        hashMap.put("language", "TH");
        hashMap.put("req_previous_status", true);
        hashMap.put("barcode", list);

        // Create request body
        RequestBody body = RequestBody.create(
                gson.toJson(gson.toJsonTree(hashMap)),
                MediaType.parse("application/json")
        );

        // Create request builder
        Request req = new Request.Builder()
                .url(this.apiUrl + "/post/api/v1/hook")
                .method("POST", body)
                .addHeader("Authorization", "Token " + this.accessToken)
                .build();

        try {
            // Do request it
            Response response = this.httpClient.newCall(req).execute();
            // Parse body
            if(response.body() != null){
                String respBody = response.body().string();
                log.info("Response body: {}", respBody);

                if(response.isSuccessful()) {
                    SubscribeByBarcodeResponse parsed = gson.fromJson(respBody, SubscribeByBarcodeResponse.class);
                    return parsed.getStatus();
                }
            }


            response.close();
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return false;
    }

    private void initToken(){
        if(!this.getToken()){
            log.warn("[Warning] Can't get access token. Please check API URL or Credential and try again.");
        }
    }

    private boolean getToken(){
        // Create request builder
        Request req = new Request.Builder()
                .url(this.apiUrl + "/post/api/v1/authenticate/token")
                .method("POST", RequestBody.create("", MediaType.parse("text/plain")))
                .addHeader("Authorization", "Token " + this.token)
                .build();

        try {
            // Do request it
            Response response = this.httpClient.newCall(req).execute();
            if(response.isSuccessful()) {
                // Parse body
                if(response.body() != null){
                    String respBody = response.body().string();
                    AuthenticateToken parsed = gson.fromJson(respBody, AuthenticateToken.class);

                    this.accessToken = parsed.getToken();
                    this.expiredIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX").parse(parsed.getExpire()).getTime() * 1000;

                    return true;
                }
            }

            response.close();
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return false;
    }
}

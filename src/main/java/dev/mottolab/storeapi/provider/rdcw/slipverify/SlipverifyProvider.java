package dev.mottolab.storeapi.provider.rdcw.slipverify;

import com.google.gson.Gson;
import dev.mottolab.storeapi.provider.rdcw.slipverify.exception.SlipVerifyError;
import dev.mottolab.storeapi.provider.rdcw.slipverify.response.SlipVerifyResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.HashMap;

@Slf4j
public class SlipverifyProvider {
    private String apiUrl = "https://suba.rdcw.co.th/v1/inquiry";
    private String clientId;
    private String clientSecret;
    private Integer postPaid;
    // HTTP Client
    private OkHttpClient httpClient = new OkHttpClient();
    // Parser
    private Gson gson = new Gson();

    public SlipverifyProvider(
            String apiUrl,
            String clientId,
            String clientSecret,
            Integer postPaid
    ) {
        this.apiUrl = apiUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.postPaid = postPaid;
    }

    public SlipVerifyResponse requestSlipVerify(String qrcode) throws SlipVerifyError {
        // Create object
        HashMap hashMap = new HashMap<>();
        hashMap.put("payload", qrcode);
        hashMap.put("postpaid", this.postPaid);

        // Request API
        Request req = new Request.Builder()
                .url(this.apiUrl)
                .method("POST", RequestBody.create(
                        gson.toJson(gson.toJsonTree(hashMap)),
                        MediaType.parse("application/json")
                ))
                .addHeader("Authorization", Credentials.basic(
                        this.clientId,
                        this.clientSecret
                ))
                .build();

        try {
            // Do request it
            Response response = this.httpClient.newCall(req).execute();

            if(response.body() != null){
                String body = response.body().string();
                log.info("Response from slip: {}", body);
                SlipVerifyResponse parsed =  gson.fromJson(body, SlipVerifyResponse.class);
                if(parsed.getCode() != 0){
                    throw new SlipVerifyError(parsed.getCode(), parsed.getMessage());
                }

                return parsed;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}

package dev.mottolab.storeapi.provider.truemoney.voucher;

import com.google.gson.Gson;
import dev.mottolab.storeapi.provider.truemoney.voucher.excpetion.TmnVoucherError;
import dev.mottolab.storeapi.provider.truemoney.voucher.response.TmnVoucherResponse;
import dev.mottolab.storeapi.provider.truemoney.voucher.response.TmnVoucherStatus;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

@Slf4j
public class TruemoneyVoucherProvider {
    private final String apiUrl = "http://gift.maythiwat.com";
    // Config
    private final String mobile;
    private final Float percentage;
    // HTTP Client
    private OkHttpClient httpClient = new OkHttpClient();
    // Parser
    private Gson gson = new Gson();

    public TruemoneyVoucherProvider(String mobile, Float percentage) {
        this.mobile = mobile;
        this.percentage = percentage;
    }

    public TmnVoucherResponse verifyVoucher(String url) throws TmnVoucherError {
        // Parse get voucherId
        String voucherId = this.getVoucherId(url);
        if (voucherId != null) {
            // Create request builder
            Request req = new Request.Builder()
                    .url(this.apiUrl + "/campaign/vouchers/" + voucherId + "/verify")
                    .addHeader("X-Requested-With", "truemoney.maythiwat.com")
                    .build();

            try {
                // Do request it
                Response response = this.httpClient.newCall(req).execute();
                // Parse body
                if(response.body() != null){
                    String rawBody = response.body().string();
                    log.info("Response from Tmn server: {}", rawBody);
                    TmnVoucherResponse body = this.gson.fromJson(rawBody, TmnVoucherResponse.class);
                    TmnVoucherStatus code = body.getStatus().code;
                    String message = body.getStatus().message;
                    if(code != TmnVoucherStatus.SUCCESS){
                        throw new TmnVoucherError(code, message);
                    }

                    return body;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;

    }

    public TmnVoucherResponse redeemVoucher(String url) throws TmnVoucherError{
        // Parse get voucherId
        String voucherId = this.getVoucherId(url);
        if (voucherId != null) {
            // Create object
            HashMap hashMap = new HashMap<>();
            hashMap.put("mobile", this.mobile);

            // Create request builder
            Request req = new Request.Builder()
                    .url(this.apiUrl + "/campaign/vouchers/" + voucherId + "/redeem")
                    .method("POST", RequestBody.create(
                            gson.toJson(gson.toJsonTree(hashMap)),
                            MediaType.parse("application/json")
                    ))
                    .addHeader("X-Requested-With", "truemoney.maythiwat.com")
                    .build();

            try {
                // Do request it
                Response response = this.httpClient.newCall(req).execute();
                // Parse body
                if(response.body() != null){
                    String rawBody = response.body().string();
                    log.info("Response from Tmn server: {}", rawBody);
                    TmnVoucherResponse body = this.gson.fromJson(rawBody, TmnVoucherResponse.class);
                    TmnVoucherStatus code = body.getStatus().code;
                    String message = body.getStatus().message;
                    if(code != TmnVoucherStatus.SUCCESS){
                        throw new TmnVoucherError(code, message);
                    }

                    return body;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;
    }

    private String getVoucherId(String url)  {
        try {
            URI parsed = new URI(url);
            if(parsed.getHost().equals("gift.truemoney.com")){
                return url.split("v=")[1];
            }

            return null;
        }catch (URISyntaxException e){
            return null;
        }
    }
}

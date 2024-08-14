package dev.mottolab.storeapi.provider.qrcr;

import com.google.gson.Gson;
import dev.mottolab.storeapi.provider.qrcr.exception.QRCRError;
import dev.mottolab.storeapi.provider.qrcr.response.QRCRResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public class QRCRProvider {
    // Api Config
    private final String apiUrl = "https://qr.rdcw.co.th";
    // HTTP Client
    private OkHttpClient httpClient = new OkHttpClient();
    // Parser
    private Gson gson = new Gson();

    public QRCRResult detectQrCode(byte[] raw) throws QRCRError {

        Request req = new Request.Builder()
                .url(this.apiUrl)
                .method("POST", RequestBody.create(raw))
                .build();

        try {
            // Do request it
            Response response = this.httpClient.newCall(req).execute();

            if(response.body() != null){
                String rawBody = response.body().string();
                log.info("Response from QR Server: {}", rawBody);

                QRCRResult result = gson.fromJson(rawBody, QRCRResult.class);
                if(!result.getCode().equals(0)){
                    throw new QRCRError(result.getCode(), result.getDetail());
                }

                return result;
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}

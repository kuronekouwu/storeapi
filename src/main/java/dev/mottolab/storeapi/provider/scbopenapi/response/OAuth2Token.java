package dev.mottolab.storeapi.provider.scbopenapi.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2Token {
    public Data data;

    @Getter
    @Setter
    public static class Data {
        public String accessToken;
        public String tokenType;
        public int expiresIn;
        public int expiresAt;
    }
}

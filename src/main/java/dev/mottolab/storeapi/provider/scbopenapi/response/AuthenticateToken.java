package dev.mottolab.storeapi.provider.scbopenapi.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateToken {
    private String token;
    private String expire;
}

package dev.mottolab.storeapi.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mottolab.storeapi.user.UserPermission;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class UserInfoDTO {
    private String account;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("joined_at")
    private Date joinedAt;
    @JsonProperty("permissions")
    private Set<UserPermission> permissions;
}

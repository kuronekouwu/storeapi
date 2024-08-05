package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.response.UserInfoDTO;
import dev.mottolab.storeapi.service.utils.UUIDService;
import dev.mottolab.storeapi.user.UserInfoDetail;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/@me")
    @ResponseBody
    public UserInfoDTO getUserInfo(Authentication authentication) {
        // Format response
        UserInfoDTO user = new UserInfoDTO();
        // Get user
        UserInfoDetail info = (UserInfoDetail) authentication.getPrincipal();
        user.setAccount(info.getUsername());
        user.setDisplayName(info.getDisplayName());
        user.setJoinedAt(new Date(UUIDService.parseTimestampUUIDV7(info.getUserId().toString())));
        user.setPermissions(info.getRoles().getPermissions());
       return user;
    }
}

package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.response.UserInfoDTO;
import dev.mottolab.storeapi.service.utils.UUIDService;
import dev.mottolab.storeapi.user.UserInfoDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public UserInfoDTO getUserInfo(@AuthenticationPrincipal UserInfoDetail principal) {
        // Format response
        UserInfoDTO user = new UserInfoDTO();
        // Get user
        user.setAccount(principal.getUsername());
        user.setDisplayName(principal.getDisplayName());
        user.setJoinedAt(new Date(UUIDService.parseTimestampUUIDV7(principal.getUserId().toString())));
        user.setPermissions(principal.getRoles().getPermissions());
       return user;
    }
}

package dev.mottolab.storeapi.controller;

import dev.mottolab.storeapi.dto.request.authen.LoginDTO;
import dev.mottolab.storeapi.dto.request.authen.RegisterDTO;
import dev.mottolab.storeapi.dto.response.authen.LoginSuccessDTO;
import dev.mottolab.storeapi.dto.response.authen.RegisterSuccessDTO;
import dev.mottolab.storeapi.exception.AccountAlreadyExist;
import dev.mottolab.storeapi.service.JwtService;
import dev.mottolab.storeapi.service.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authen")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserInfoService userInfoService;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserInfoService userInfoService,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @ResponseBody
    public RegisterSuccessDTO register(
            @Valid @RequestBody RegisterDTO registerDTO
    ) throws AccountAlreadyExist  {
        // Register user
        this.userInfoService.registerUser(registerDTO);
        // Create DTO
        RegisterSuccessDTO registerSuccessDTO = new RegisterSuccessDTO();
        registerSuccessDTO.setMessage("Register has been completed.");

        return registerSuccessDTO;
    }

    @PostMapping("/login")
    @ResponseBody
    public LoginSuccessDTO login(@Valid @RequestBody LoginDTO auth){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(auth.account(), auth.password())
        );
        if(authentication.isAuthenticated()){
            String accessToken = this.jwtService.createAccessToken(auth.account());
            // Create response
            LoginSuccessDTO loginSuccessDTO = new LoginSuccessDTO();
            loginSuccessDTO.setAccessToken(accessToken);
            return loginSuccessDTO;
        }

        throw new UsernameNotFoundException("Username or password is incorrect.");
    }
}

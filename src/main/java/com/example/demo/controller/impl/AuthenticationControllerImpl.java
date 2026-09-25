package com.example.demo.controller.impl;

import com.example.demo.controller.AuthenticationController;
import com.example.demo.controller.RestBaseController;
import com.example.demo.controller.RootEntity;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.DtoUser;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationControllerImpl extends RestBaseController  implements AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/register")
    public RootEntity<DtoUser> register(@Valid @RequestBody AuthRequest authRequest){
        return ok(authenticationService.register(authRequest));
    }
    @PostMapping("/authenticate")
    public RootEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest authRequest){
        return ok(authenticationService.authenticate(authRequest));
    }
    @PostMapping ("/refreshToken")
    public RootEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        return ok(authenticationService.refreshToken(request));
    }
}

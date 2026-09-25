package com.example.demo.controller;


import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.DtoUser;

public interface AuthenticationController {
    public RootEntity<DtoUser> register(AuthRequest authRequest);
}

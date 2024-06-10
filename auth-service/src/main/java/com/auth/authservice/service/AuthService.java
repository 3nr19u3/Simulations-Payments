package com.auth.authservice.service;


import com.auth.authservice.payload.RegisterDto;
import com.auth.authservice.payload.LoginDto;


public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}

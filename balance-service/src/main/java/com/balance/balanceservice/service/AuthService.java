package com.balance.balanceservice.service;


import com.balance.balanceservice.payload.LoginDto;
import com.balance.balanceservice.payload.RegisterDto;


public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}

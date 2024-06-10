package com.auth.authservice.service;

import com.auth.authservice.payload.UserDto;

public interface UserService {
    UserDto getUserById(Long id);
    UserDto getUserByUsername(String username);
    UserDto getUserByEmail(String username);
}

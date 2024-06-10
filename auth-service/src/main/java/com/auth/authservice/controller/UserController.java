package com.auth.authservice.controller;

import com.auth.authservice.payload.UserDto;
import com.auth.authservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") long id) throws Exception {
        LOGGER.info("INTO getUserById()");
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("username") String username) throws Exception{
        LOGGER.info("INTO getUserByEmail()");
        return ResponseEntity.ok(userService.getUserByEmail(username));
    }
}

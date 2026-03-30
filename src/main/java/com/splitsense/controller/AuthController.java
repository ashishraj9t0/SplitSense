package com.splitsense.controller;

import com.splitsense.dto.request.UserRequest;
import com.splitsense.dto.response.UserResponse;
import com.splitsense.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) {
        UserResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }
}

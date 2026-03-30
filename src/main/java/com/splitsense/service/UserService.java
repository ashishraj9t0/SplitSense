package com.splitsense.service;

import com.splitsense.domain.entity.User;
import com.splitsense.dto.request.UserRequest;
import com.splitsense.dto.response.UserResponse;
import com.splitsense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());

        return response;
    }

    // To get user
    public UserResponse loginUser(UserRequest request)
    {
        if ((request.getUsername() == null || request.getUsername().isBlank()) &&
        (request.getEmail() == null || request.getEmail().isBlank())) {
        throw new RuntimeException("Username or email is required");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        User user = userRepository
                    .findByUsernameOrEmail(request.getUsername(), request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
                if(Objects.isNull(user))
        {
            throw new RuntimeException("user not found");
        }

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new RuntimeException("incorrect password");
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }
}

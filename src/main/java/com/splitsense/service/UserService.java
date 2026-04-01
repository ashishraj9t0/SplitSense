package com.splitsense.service;

import com.splitsense.domain.entity.EmailOtp;
import com.splitsense.domain.entity.User;
import com.splitsense.dto.request.EmailOptRequest;
import com.splitsense.dto.request.UserRequest;
import com.splitsense.dto.response.UserResponse;
import com.splitsense.repository.EmailOtpRepository;
import com.splitsense.repository.UserRepository;
import com.splitsense.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailOtpRepository emailOtpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    public UserService(
            UserRepository userRepository,
            EmailOtpRepository emailOtpRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.emailOtpRepository = emailOtpRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    // user sign up request
    public UserResponse registerUser(UserRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new RuntimeException("Username is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if(!verifyOtp(request.getEmail(), request.getOtp().toString()))
        {
            throw new RuntimeException("Otp not verified");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());

        return response;
    }

    // User Login Request
    public UserResponse loginUser(UserRequest request) {
        String username = request.getUsername() != null ? request.getUsername().trim() : null;
        String email = request.getEmail() != null ? request.getEmail().trim() : null;

        if ((username == null || username.isBlank()) && (email == null || email.isBlank())) {
            throw new RuntimeException("Username or email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        User user = userRepository
                .findByUsernameOrEmail(username, email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername());

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setToken(token);

        return response;
    }

    // Email Opt Verification Request

    public Boolean optGeneration(EmailOptRequest request)
    {
        try {
            EmailOtp emailOtp = emailOtpRepository.findByEmail(request.getEmail());

            if(!(emailOtp == null))
            {
                if(emailOtp.getExpiryTime().isBefore(LocalDateTime.now())) emailOtp.setOtp(generateOtp());
                emailOtp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
                EmailOtp response = emailOtpRepository.save(emailOtp);

                return emailService.sendOtpEmail(response.getEmail(), String.valueOf(response.getOtp()));
            }
            EmailOtp email = new EmailOtp();
            email.setEmail(request.getEmail());
            email.setOtp(generateOtp());
            email.setExpiryTime(LocalDateTime.now().plusMinutes(5));
            email.setVerified(false);
            email.setCreatedOn(LocalDateTime.now());

            EmailOtp response = emailOtpRepository.save(email);
            if (response.getId() == null) {
                return false;
            }

            return emailService.sendOtpEmail(response.getEmail(), String.valueOf(response.getOtp()));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyOtp(String email, String otp) {

        var savedOtp = emailOtpRepository.findByEmail(email);

        if(savedOtp == null)
            throw new RuntimeException("OTP not found");

        if(savedOtp.getExpiryTime().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");

        if(!(String.valueOf(savedOtp.getOtp()).equals(otp)))
            throw new RuntimeException("Invalid OTP");

        savedOtp.setVerified(true);
        var save = emailOtpRepository.save(savedOtp);

        return save.getId() != null;
    }

    private int generateOtp()
    {
        return new Random().nextInt(900000) + 100000;
    }
}

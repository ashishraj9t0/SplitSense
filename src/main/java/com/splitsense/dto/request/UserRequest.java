package com.splitsense.dto.request;

public class UserRequest {
    private String username;
    private String email;
    private String password;
    private Long otp;

    // Constructors, getters, setters
    public UserRequest() {}

    public UserRequest(String username, String email, String password, Long otp) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.otp = otp;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public void setOtp(Long otp) { this.otp = otp; }
    public Long getOtp() { return otp; }
}

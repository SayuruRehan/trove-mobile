package com.example.trove_mobile.auth;

public class RegisterRequest {
    private String username;
    private int phoneNumber;
    private String email;
    private String address;
    private String role;
    private String password;

    public RegisterRequest(String username, int phoneNumber, String email, String address, String role, String password) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.role = role;
        this.password = password;
    }
}


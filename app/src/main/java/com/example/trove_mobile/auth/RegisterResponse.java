package com.example.trove_mobile.auth;

public class RegisterResponse {
    private String message;
    private User user;

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public class User {
        private String userId;
        private String username;
        private int phoneNumber;
        private String email;
        private String address;
        private String role;
        private String password;
    }
}

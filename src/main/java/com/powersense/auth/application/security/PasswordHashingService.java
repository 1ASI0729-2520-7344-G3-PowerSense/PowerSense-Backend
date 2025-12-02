package com.powersense.auth.application.security;

public interface PasswordHashingService {
    String hashPassword(String rawPassword);
    boolean verifyPassword(String rawPassword, String hashedPassword);
}
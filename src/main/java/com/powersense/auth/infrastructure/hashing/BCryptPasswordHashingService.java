package com.powersense.auth.infrastructure.hashing;

import com.powersense.auth.application.security.PasswordHashingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordHashingService implements PasswordHashingService {

    private final BCryptPasswordEncoder encoder;

    public BCryptPasswordHashingService() {
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) {
            return false;
        }
        return encoder.matches(rawPassword, hashedPassword);
    }
}
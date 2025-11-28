package com.powersense.auth.application.internal.outboundservices.tokens;

import com.powersense.auth.domain.model.aggregates.User;

public interface TokenService {
    String generateToken(User user);
    Long extractUserId(String token);
    boolean validateToken(String token);
    String refreshToken(String token);
}
package com.powersense.auth.interfaces.rest.resources;

public record TokenRefreshResponse(
        String token,
        String refreshToken) {
}

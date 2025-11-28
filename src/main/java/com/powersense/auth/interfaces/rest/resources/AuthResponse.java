package com.powersense.auth.interfaces.rest.resources;

public record AuthResponse(
        UserResource user,
        String token
) {
}
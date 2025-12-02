package com.powersense.auth.application.internal.outboundservices.tokens;

import com.powersense.auth.domain.model.aggregates.User;

public interface TokenService {

    // Genera un token JWT basado en la información del usuario
    String generateToken(User user);

    // Extrae el ID del usuario contenido dentro del token
    Long extractUserId(String token);

    // Verifica si el token es válido (firma, expiración, estructura)
    boolean validateToken(String token);

    // Crea un nuevo token basado en uno existente (generalmente cuando está por expirar)
    String refreshToken(String token);
}

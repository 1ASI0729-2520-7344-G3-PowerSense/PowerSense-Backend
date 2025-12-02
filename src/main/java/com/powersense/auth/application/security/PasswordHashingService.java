package com.powersense.auth.application.security;

public interface PasswordHashingService {

    // Genera el hash seguro de una contraseña en texto plano
    String hashPassword(String rawPassword);

    // Verifica si la contraseña ingresada coincide con el hash almacenado
    boolean verifyPassword(String rawPassword, String hashedPassword);
}

package com.powersense.auth.application.internal.commandservices;

import com.powersense.auth.application.internal.outboundservices.tokens.TokenService;
import com.powersense.auth.application.security.PasswordHashingService;
import com.powersense.auth.domain.model.aggregates.User;
import com.powersense.auth.domain.model.commands.LoginUserCommand;
import com.powersense.auth.domain.model.queries.GetUserByEmailQuery;
import com.powersense.auth.domain.services.UserQueryService;
import org.springframework.stereotype.Service;
import com.powersense.auth.domain.model.queries.GetUserByIdQuery;

@Service
public class AuthenticationService {

    private final UserQueryService userQueryService;
    private final PasswordHashingService passwordHashingService;
    private final TokenService tokenService;

    public AuthenticationService(
            UserQueryService userQueryService,
            PasswordHashingService passwordHashingService,
            TokenService tokenService
    ) {
        this.userQueryService = userQueryService;
        this.passwordHashingService = passwordHashingService;
        this.tokenService = tokenService;
    }

    public AuthenticationResult authenticate(LoginUserCommand command) {
        // Buscar usuario por email
        User user = userQueryService.handle(new GetUserByEmailQuery(command.email()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        // Verificar contraseña
        if (!passwordHashingService.verifyPassword(command.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Generar token
        String token = tokenService.generateToken(user);

        return new AuthenticationResult(user, token);
    }

    public User getCurrentUser(String token) {
        if (!tokenService.validateToken(token)) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        Long userId = tokenService.extractUserId(token);
        return userQueryService.handle(new GetUserByIdQuery(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public record AuthenticationResult(User user, String token) {}
}
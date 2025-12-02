package com.powersense.auth.interfaces.rest.controllers;

import com.powersense.auth.application.internal.commandservices.AuthenticationService;
import com.powersense.auth.application.internal.commandservices.AuthenticationService.AuthenticationResult;
import com.powersense.auth.domain.model.aggregates.User;
import com.powersense.auth.domain.model.commands.LoginUserCommand;
import com.powersense.auth.domain.model.commands.RegisterUserCommand;
import com.powersense.auth.domain.model.commands.UpdateUserProfileCommand;
import com.powersense.auth.domain.model.queries.GetUserByIdQuery;
import com.powersense.auth.domain.services.UserCommandService;
import com.powersense.auth.domain.services.UserQueryService;
import com.powersense.auth.application.internal.outboundservices.tokens.TokenService;
import com.powersense.auth.interfaces.rest.resources.*;
import com.powersense.auth.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication and user management endpoints")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    public AuthController(
            UserCommandService userCommandService,
            UserQueryService userQueryService,
            AuthenticationService authenticationService,
            TokenService tokenService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterUserResource resource) {
        try {
            // Convertir resource a command
            RegisterUserCommand command = RegisterUserCommandFromResourceAssembler
                    .toCommandFromResource(resource);

            // Ejecutar comando
            User user = userCommandService.handle(command);

            // Crear comando de login para generar token
            LoginUserCommand loginCommand = new LoginUserCommand(resource.email(), resource.password());
            AuthenticationResult authResult = authenticationService.authenticate(loginCommand);

            // Convertir a response
            UserResource userResource = UserResourceFromEntityAssembler.toResourceFromEntity(authResult.user());
            AuthResponse response = new AuthResponse(userResource, authResult.token(), null);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginUserResource resource) {
        try {
            // Convertir resource a command
            LoginUserCommand command = LoginUserCommandFromResourceAssembler
                    .toCommandFromResource(resource);

            // Autenticar
            AuthenticationResult authResult = authenticationService.authenticate(command);

            // Convertir a response
            UserResource userResource = UserResourceFromEntityAssembler.toResourceFromEntity(authResult.user());
            AuthResponse response = new AuthResponse(userResource, authResult.token(), null);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user")
    public ResponseEntity<UserResource> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Long userId = (Long) authentication.getPrincipal();
            User user = userQueryService.handle(new GetUserByIdQuery(userId))
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            UserResource userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user);
            return ResponseEntity.ok(userResource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/profile")
    @Operation(summary = "Update user profile")
    public ResponseEntity<UserResource> updateProfile(
            @Valid @RequestBody UpdateUserProfileResource resource,
            Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Long userId = (Long) authentication.getPrincipal();

            // Convertir resource a command
            UpdateUserProfileCommand command = UpdateUserProfileCommandFromResourceAssembler
                    .toCommandFromResource(userId, resource);

            // Ejecutar comando
            User updatedUser = userCommandService.handle(command);

            // Convertir a response
            UserResource userResource = UserResourceFromEntityAssembler.toResourceFromEntity(updatedUser);
            return ResponseEntity.ok(userResource);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/verify")
    @Operation(summary = "Verify if JWT token is valid")
    public ResponseEntity<TokenValidationResponse> verifyToken(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.ok(new TokenValidationResponse(false));
            }
            return ResponseEntity.ok(new TokenValidationResponse(true));
        } catch (Exception e) {
            return ResponseEntity.ok(new TokenValidationResponse(false));
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Long userId = (Long) authentication.getPrincipal();
            User user = userQueryService.handle(new GetUserByIdQuery(userId))
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Generar nuevo token
            String newToken = tokenService.generateToken(user);
            TokenRefreshResponse response = new TokenRefreshResponse(newToken, null);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout (client-side token removal)")
    public ResponseEntity<Void> logout() {
        // En JWT stateless, el logout es manejado en el cliente
        // Este endpoint es principalmente para consistencia con la API
        return ResponseEntity.ok().build();
    }
}
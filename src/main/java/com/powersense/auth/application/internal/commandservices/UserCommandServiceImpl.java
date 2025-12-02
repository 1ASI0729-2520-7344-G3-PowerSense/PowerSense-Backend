package com.powersense.auth.application.internal.commandservices;

import com.powersense.auth.application.security.PasswordHashingService;
import com.powersense.auth.domain.model.aggregates.User;
import com.powersense.auth.domain.model.commands.RegisterUserCommand;
import com.powersense.auth.domain.model.commands.UpdateUserProfileCommand;
import com.powersense.auth.domain.model.queries.GetUserByEmailQuery;
import com.powersense.auth.domain.model.queries.GetUserByIdQuery;
import com.powersense.auth.domain.services.UserCommandService;
import com.powersense.auth.domain.services.UserQueryService;
import com.powersense.auth.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final PasswordHashingService passwordHashingService;
    private final UserQueryService userQueryService;

    public UserCommandServiceImpl(
            UserRepository userRepository,
            PasswordHashingService passwordHashingService,
            UserQueryService userQueryService
    ) {
        this.userRepository = userRepository;
        this.passwordHashingService = passwordHashingService;
        this.userQueryService = userQueryService;
    }

    @Override
    @Transactional
    public User handle(RegisterUserCommand command) {
        // Verificar si el email ya existe
        var existingUser = userQueryService.handle(new GetUserByEmailQuery(command.email()));
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already registered: " + command.email());
        }

        // Hash de la contraseña
        String hashedPassword = passwordHashingService.hashPassword(command.password());

        // Crear nuevo usuario
        User user = new User(command, hashedPassword);

        // Guardar usuario
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User handle(UpdateUserProfileCommand command) {
        // Buscar usuario
        User user = userQueryService.handle(new GetUserByIdQuery(command.userId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + command.userId()));

        // Actualizar perfil
        user.updateProfile(command);

        // Guardar cambios
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        // Buscar usuario
        User user = userQueryService.handle(new GetUserByIdQuery(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Desactivar en lugar de eliminar (soft delete)
        user.deactivate();
        userRepository.save(user);
    }
}
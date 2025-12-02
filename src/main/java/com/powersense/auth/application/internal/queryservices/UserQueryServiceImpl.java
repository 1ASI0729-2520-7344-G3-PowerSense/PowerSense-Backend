package com.powersense.auth.application.internal.queryservices;

import com.powersense.auth.domain.model.aggregates.User;
import com.powersense.auth.domain.model.queries.GetUserByEmailQuery;
import com.powersense.auth.domain.model.queries.GetUserByIdQuery;
import com.powersense.auth.domain.services.UserQueryService;
import com.powersense.auth.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true) // Solo consultas, sin modificaciones
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    // Inyección del repositorio JPA
    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        // Busca por ID y devuelve solo si el usuario está activo
        return userRepository.findById(query.userId())
                .filter(User::getIsActive);
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        // Busca por email y devuelve solo si el usuario está activo
        return userRepository.findByEmail_Value(query.email())
                .filter(User::getIsActive);
    }
}

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
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId())
                .filter(User::getIsActive); // Solo usuarios activos
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail_Value(query.email())
                .filter(User::getIsActive); // Solo usuarios activos
    }
}
package com.powersense.auth.domain.services;

import com.powersense.auth.domain.model.aggregates.User;
import com.powersense.auth.domain.model.queries.GetUserByEmailQuery;
import com.powersense.auth.domain.model.queries.GetUserByIdQuery;

import java.util.Optional;

public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByEmailQuery query);
}
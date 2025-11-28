package com.powersense.auth.domain.services;

import com.powersense.auth.domain.model.aggregates.User;
import com.powersense.auth.domain.model.commands.RegisterUserCommand;
import com.powersense.auth.domain.model.commands.UpdateUserProfileCommand;

public interface UserCommandService {
    User handle(RegisterUserCommand command);
    User handle(UpdateUserProfileCommand command);
    void deleteUser(Long userId);
}
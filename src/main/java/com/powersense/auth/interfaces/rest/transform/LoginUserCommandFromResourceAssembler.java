package com.powersense.auth.interfaces.rest.transform;

import com.powersense.auth.domain.model.commands.LoginUserCommand;
import com.powersense.auth.interfaces.rest.resources.LoginUserResource;

public class LoginUserCommandFromResourceAssembler {

    public static LoginUserCommand toCommandFromResource(LoginUserResource resource) {
        return new LoginUserCommand(
                resource.email(),
                resource.password()
        );
    }
}
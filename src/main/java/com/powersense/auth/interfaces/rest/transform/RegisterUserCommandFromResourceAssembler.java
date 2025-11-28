package com.powersense.auth.interfaces.rest.transform;

import com.powersense.auth.domain.model.commands.RegisterUserCommand;
import com.powersense.auth.interfaces.rest.resources.RegisterUserResource;

public class RegisterUserCommandFromResourceAssembler {

    public static RegisterUserCommand toCommandFromResource(RegisterUserResource resource) {
        return new RegisterUserCommand(
                resource.email(),
                resource.password(),
                resource.name()
        );
    }
}
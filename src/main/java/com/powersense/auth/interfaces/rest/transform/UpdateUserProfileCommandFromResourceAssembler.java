package com.powersense.auth.interfaces.rest.transform;

import com.powersense.auth.domain.model.commands.UpdateUserProfileCommand;
import com.powersense.auth.interfaces.rest.resources.UpdateUserProfileResource;

public class UpdateUserProfileCommandFromResourceAssembler {

    public static UpdateUserProfileCommand toCommandFromResource(
            Long userId,
            UpdateUserProfileResource resource
    ) {
        return new UpdateUserProfileCommand(
                userId,
                resource.name(),
                resource.avatarUrl()
        );
    }
}
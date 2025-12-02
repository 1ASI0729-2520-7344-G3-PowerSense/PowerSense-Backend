package com.powersense.auth.interfaces.rest.transform;

import com.powersense.auth.domain.model.aggregates.User;
import com.powersense.auth.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {

    public static UserResource toResourceFromEntity(User user) {
        return new UserResource(
                user.getId(),
                user.getEmailValue(),
                user.getName(),
                user.getAvatarUrl(),
                user.getIsActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
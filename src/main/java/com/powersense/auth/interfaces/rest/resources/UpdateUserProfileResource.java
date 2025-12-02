package com.powersense.auth.interfaces.rest.resources;

import jakarta.validation.constraints.Size;

public record UpdateUserProfileResource(

        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        String avatarUrl
) {
}
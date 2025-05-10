package com.magic.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(
        Long id,

        @NotEmpty(message = "username is required.")
        String username,

        @NotEmpty(message = "roles are required.")
        String roles,

        boolean enabled
) {
}

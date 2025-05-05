package com.magic.user;

public record SiteUserDto(
        Long id,
        String username,
        String roles,
        boolean enabled
) {
}

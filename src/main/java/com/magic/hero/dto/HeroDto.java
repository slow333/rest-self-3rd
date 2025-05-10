package com.magic.hero.dto;

import jakarta.validation.constraints.NotEmpty;

public record HeroDto(
        Integer id,
        @NotEmpty(message = "hero name is required.")
        String name,
        Integer numberOfMagic
) {
}

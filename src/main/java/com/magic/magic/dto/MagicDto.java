package com.magic.magic.dto;

import com.magic.hero.dto.HeroDto;
import jakarta.validation.constraints.NotEmpty;

public record MagicDto(
        String id,

        @NotEmpty(message = "이름은 필수입니다.")
        String name,

        @NotEmpty(message = "설명은 필수입니다.")
        String description,

        HeroDto owner
) {
}

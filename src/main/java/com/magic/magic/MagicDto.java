package com.magic.magic;

import com.magic.hero.Hero;
import com.magic.hero.HeroDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

public record MagicDto(
        String id,

        @NotEmpty(message = "이름은 필수입니다.")
        String name,

        @NotEmpty(message = "설명은 필수입니다.")
        String description,

        HeroDto owner
) {
}

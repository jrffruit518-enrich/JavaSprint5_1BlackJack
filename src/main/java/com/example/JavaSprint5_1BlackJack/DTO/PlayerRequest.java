package com.example.JavaSprint5_1BlackJack.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PlayerRequest(
        @Schema(description = "The unique nickname of the player", example = "JohnDoe")
        @NotBlank(message = "Player name must not be blank")
        String playerName
) {
}

package com.example.JavaSprint5_1BlackJack.DTO;

import com.example.JavaSprint5_1BlackJack.enums.MoveType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PlayRequest(
        @Schema(description = "Action: HIT to get a card, STAND to stop", example = "HIT")
        @NotNull(message = "Move type is mandatory")
        MoveType moveType
) {
}

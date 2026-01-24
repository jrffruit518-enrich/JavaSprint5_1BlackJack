package com.example.JavaSprint5_1BlackJack.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record Ranking(
        @NotNull
        @Schema(description = "Position", example = "1")
        Integer position,

        @NotNull
        @Schema(description = "playerName", example = "PlayerOne")
        String playerName,

        @NotNull
        @Schema(description = "Total games played", example = "50")
        Integer totalGames,

        @Schema(description = "Total wins", example = "20")
        @NotNull
                Integer totalWin,

        @Schema(description = "Win rate", example = "0.5")
        @NotNull
        Double winRate
) {
}

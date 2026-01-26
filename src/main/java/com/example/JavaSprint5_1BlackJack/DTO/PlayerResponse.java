package com.example.JavaSprint5_1BlackJack.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlayerResponse(
        @Schema(description = "Position", example = "1")
        Integer position,

        @Schema(description = "playerName", example = "PlayerOne")
        String playerName,

        @Schema(description = "Total wins", example = "20")
        Integer totalWins,

        @Schema(description = "Total games played", example = "50")
        Integer totalGames,


        @Schema(description = "Win rate", example = "0.5")
        Double winRate
) {


}

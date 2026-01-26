package com.example.JavaSprint5_1BlackJack.DTO;

import com.example.JavaSprint5_1BlackJack.enums.GameResult;
import com.example.JavaSprint5_1BlackJack.enums.GameStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record GameResponse(
        @Schema(description = "The unique game ID from MongoDB", example = "60d5f50c2f352a70")
        String gameId,

        @Schema(description = "Name of the player", example = "JohnDoe")
        String playerName,

        @Schema(description = "Visible dealer cards")
        List<CardResponse> dealerCards,

        @Schema(description = "Player cards")
        List<CardResponse> playerCards,

        // --- Added Fields ---
        @Schema(description = "Total value of visible dealer cards", example = "11")
        Integer dealerVisibleValue,

        @Schema(description = "Total value of player's hand", example = "21")
        Integer playerValue,
        // --------------------

        @Schema(description = "Current stage of the game", example = "PLAYER_TURN")
        GameStatus gameStatus,

        @Schema(description = "Outcome of the game", example = "UNDECIDED")
        GameResult gameResult
) {
}
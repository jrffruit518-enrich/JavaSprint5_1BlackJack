package com.example.JavaSprint5_1BlackJack.DTO;

import com.example.JavaSprint5_1BlackJack.entities.Deck;
import com.example.JavaSprint5_1BlackJack.entities.Hand;
import com.example.JavaSprint5_1BlackJack.enums.GameResult;
import com.example.JavaSprint5_1BlackJack.enums.GameStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GameRequest(
        @Schema(description = "Database ID of the player", example = "101")
        @NotNull(message = "Player ID is mandatory")
        Long playerId,

        @Schema(description = "Displayed name of the player", example = "JohnDoe")
        @NotBlank(message = "Player name is mandatory")
        String playerName
   ) {

}

package com.example.JavaSprint5_1BlackJack.DTO;

import com.example.JavaSprint5_1BlackJack.entities.Player;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PlayerRankingResponse(
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

        public static PlayerRankingResponse fromEntity(Player player, int position) {
               double winRate = (player.getTotalGames()==null||player.getTotalGames()==0)?0.0:
                       (double)player.getTotalWins()/player.getTotalGames();
               return new PlayerRankingResponse(
                       position,
                       player.getPlayerName(),
                       player.getTotalWins(),
                       player.getTotalGames(),
                       Math.round(winRate * 100.0) / 100.0
               );
        }
}

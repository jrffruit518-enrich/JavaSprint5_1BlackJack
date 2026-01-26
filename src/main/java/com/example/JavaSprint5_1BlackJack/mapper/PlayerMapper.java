package com.example.JavaSprint5_1BlackJack.mapper;

import com.example.JavaSprint5_1BlackJack.DTO.PlayerResponse;
import com.example.JavaSprint5_1BlackJack.entities.Player;



public class PlayerMapper {
    public static PlayerResponse toResponse(Player player, int position) {
        double winRate = (player.getTotalGames()==null||player.getTotalGames()==0)?0.0:
                (double)player.getTotalWins()/player.getTotalGames();
        return new PlayerResponse(
                position,
                player.getPlayerId(),
                player.getPlayerName(),
                player.getTotalWins(),
                player.getTotalGames(),
                Math.round(winRate * 100.0) / 100.0
        );
    }
}

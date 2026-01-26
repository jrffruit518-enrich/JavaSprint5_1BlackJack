package com.example.JavaSprint5_1BlackJack.services;

import com.example.JavaSprint5_1BlackJack.DTO.PlayerResponse;
import com.example.JavaSprint5_1BlackJack.DTO.PlayerRequest;
import com.example.JavaSprint5_1BlackJack.entities.Player;
import com.example.JavaSprint5_1BlackJack.enums.GameResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {
    Mono<PlayerResponse> createPlayer(PlayerRequest request);

    Mono<PlayerResponse>  findPlayerById(Long id);

    Flux<PlayerResponse> findAllPlayers();

    Mono<PlayerResponse> updatePlayerById(Long id, PlayerRequest request);

    Mono<Void> deletePlayerById(Long id);

    public Mono<Player> updatePlayerStats(Long playerId, GameResult result);
}

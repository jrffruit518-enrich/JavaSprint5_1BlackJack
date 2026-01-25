package com.example.JavaSprint5_1BlackJack.services;

import com.example.JavaSprint5_1BlackJack.DTO.PlayerRankingResponse;
import com.example.JavaSprint5_1BlackJack.DTO.PlayerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PlayerService {
    Mono<PlayerRankingResponse> createPlayer(PlayerRequest request);

    Mono<PlayerRankingResponse>  findPlayerById(Long id);

    Flux<PlayerRankingResponse> findAllPlayers();

    Mono<PlayerRankingResponse> updatePlayerById(Long id, PlayerRequest request);

    Mono<Void> deletePlayerById(Long id);
}

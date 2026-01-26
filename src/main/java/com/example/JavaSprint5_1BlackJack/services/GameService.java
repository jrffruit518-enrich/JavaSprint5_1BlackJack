package com.example.JavaSprint5_1BlackJack.services;

import com.example.JavaSprint5_1BlackJack.DTO.GameRequest;
import com.example.JavaSprint5_1BlackJack.DTO.GameResponse;
import com.example.JavaSprint5_1BlackJack.DTO.PlayRequest;
import reactor.core.publisher.Mono;

public interface GameService {
    // Create a new game: POST /game/new
    Mono<GameResponse> createGame(GameRequest request);

    // Get game details: GET /game/{id}
    Mono<GameResponse> findGameById(String id);

    // Perform a move: POST /game/{id}/play
    // You might need a PlayRequest DTO here
    Mono<GameResponse> playGame(String id, PlayRequest request);

    // Delete a game: DELETE /game/{id}/delete
    Mono<Void> deleteGameById(String id);
}

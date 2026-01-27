package com.example.JavaSprint5_1BlackJack.controller;

import com.example.JavaSprint5_1BlackJack.DTO.GameRequest;
import com.example.JavaSprint5_1BlackJack.DTO.GameResponse;
import com.example.JavaSprint5_1BlackJack.DTO.PlayRequest;
import com.example.JavaSprint5_1BlackJack.controllers.GameController;
import com.example.JavaSprint5_1BlackJack.enums.GameResult;
import com.example.JavaSprint5_1BlackJack.enums.GameStatus;
import com.example.JavaSprint5_1BlackJack.enums.MoveType;
import com.example.JavaSprint5_1BlackJack.exception.ResourceNotFoundException;
import com.example.JavaSprint5_1BlackJack.services.GameServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GameServiceImp gameService; // Mocking the interface

    private GameResponse mockResponse;

    @BeforeEach
    void setUp() {
        // Initialize a standard mock response for successful cases
        mockResponse = new GameResponse(
                "game123",
                "PlayerOne",
                List.of(), // dealerCards
                List.of(), // playerCards
                0,         // dealerVisibleValue
                0,         // playerValue
                GameStatus.PLAYER_TURN,
                GameResult.UNDECIDED
        );
    }

    // --- 1. createGame Tests ---

    @Test
    void createGame_Success() {
        GameRequest request = new GameRequest(1L, "PlayerOne");
        when(gameService.createGame(any(GameRequest.class))).thenReturn(Mono.just(mockResponse));

        webTestClient.post()
                .uri("/blackjack/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.gameId").isEqualTo("game123")
                .jsonPath("$.playerName").isEqualTo("PlayerOne");
    }

    @Test
    void createGame_Failure_InvalidRequest() {
        // PlayerName is blank, should trigger @Valid failure
        GameRequest invalidRequest = new GameRequest(1L, "");

        webTestClient.post()
                .uri("/blackjack/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    // --- 2. findGameById Tests ---

    @Test
    void findGameById_Success() {
        String id = "game123";
        when(gameService.findGameById(id)).thenReturn(Mono.just(mockResponse));

        webTestClient.get()
                .uri("/blackjack/game/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.gameId").isEqualTo(id);
    }

    @Test
    void findGameById_Failure_NotFound() {
        String id = "nonexistent";
        when(gameService.findGameById(id))
                .thenReturn(Mono.error(new ResourceNotFoundException("Game not found")));

        webTestClient.get()
                .uri("/blackjack/game/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }

    // --- 3. playGame Tests ---

    @Test
    void playGame_Success() {
        String id = "game123";
        PlayRequest request = new PlayRequest(MoveType.HIT);
        when(gameService.playGame(eq(id), any(PlayRequest.class))).thenReturn(Mono.just(mockResponse));

        webTestClient.post()
                .uri("/blackjack/game/{id}/play", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.gameStatus").isEqualTo("PLAYER_TURN");
    }

    @Test
    void playGame_Failure_IllegalState() {
        String id = "game123";
        PlayRequest request = new PlayRequest(MoveType.HIT);
        // Simulate game already finished
        when(gameService.playGame(eq(id), any(PlayRequest.class)))
                .thenReturn(Mono.error(new IllegalStateException("Game is already finished")));

        webTestClient.post()
                .uri("/blackjack/game/{id}/play", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(409); // Conflict (mapped in GlobalExceptionHandler)
    }

    // --- 4. deleteGameById Tests ---

    @Test
    void deleteGameById_Success() {
        String id = "game123";
        when(gameService.deleteGameById(id)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/blackjack/game/{id}/delete", id)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteGameById_Failure_NotFound() {
        String id = "game123";
        when(gameService.deleteGameById(id))
                .thenReturn(Mono.error(new ResourceNotFoundException("Game not found")));

        webTestClient.delete()
                .uri("/blackjack/game/{id}/delete", id)
                .exchange()
                .expectStatus().isNotFound();
    }
}

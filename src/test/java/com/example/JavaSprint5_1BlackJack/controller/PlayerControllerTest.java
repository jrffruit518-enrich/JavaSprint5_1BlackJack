package com.example.JavaSprint5_1BlackJack.controller;


import com.example.JavaSprint5_1BlackJack.DTO.PlayerRequest;
import com.example.JavaSprint5_1BlackJack.DTO.PlayerResponse;
import com.example.JavaSprint5_1BlackJack.exception.ResourceNotFoundException;
import com.example.JavaSprint5_1BlackJack.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(PlayerControllerTest.class)
public class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PlayerService playerService;

    @Test
    void createPlayer_Success() {
        PlayerRequest request = new PlayerRequest("JohnDoe");
        PlayerResponse response = new PlayerResponse(1, "JohnDoe", 0, 0, 0.0);

        Mockito.when(playerService.createPlayer(any(PlayerRequest.class)))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/blackjack/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.playerName").isEqualTo("JohnDoe");
    }

    @Test
    void createPlayer_Failure_BlankName() {
        // PlayerRequest with blank name to trigger @Valid
        PlayerRequest request = new PlayerRequest("");

        webTestClient.post()
                .uri("/blackjack/players")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest(); // Expecting 400 due to validation
    }

    // --- Find Player By ID Tests ---

    @Test
    void findPlayerById_Success() {
        Long id = 1L;
        PlayerResponse response = new PlayerResponse(1, "JohnDoe", 0, 0, 0.0);

        Mockito.when(playerService.findPlayerById(id))
                .thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/blackjack/players/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.playerName").isEqualTo("JohnDoe");
    }

    @Test
    void findPlayerById_Failure_NotFound() {
        Long id = 99L;
        Mockito.when(playerService.findPlayerById(id))
                .thenReturn(Mono.error(new ResourceNotFoundException("Player not found")));

        webTestClient.get()
                .uri("/blackjack/players/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }

    // --- Find All Players Tests ---

    @Test
    void findAllPlayers_Success() {
        PlayerResponse p1 = new PlayerResponse(1, "A", 10, 20, 0.5);
        PlayerResponse p2 = new PlayerResponse(2, "B", 5, 20, 0.25);

        Mockito.when(playerService.findAllPlayers()).thenReturn(Flux.just(p1, p2));

        webTestClient.get()
                .uri("/blackjack/players")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PlayerResponse.class)
                .hasSize(2);
    }

    // --- Update Player Tests ---

    @Test
    void updatePlayer_Success() {
        Long id = 1L;
        PlayerRequest request = new PlayerRequest("NewName");
        PlayerResponse response = new PlayerResponse(1, "NewName", 0, 0, 0.0);

        Mockito.when(playerService.updatePlayerById(eq(id), any(PlayerRequest.class)))
                .thenReturn(Mono.just(response));

        webTestClient.put()
                .uri("/blackjack/players/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.playerName").isEqualTo("NewName");
    }

    @Test
    void updatePlayer_Failure_InvalidID() {
        Long id = 99L;
        PlayerRequest request = new PlayerRequest("NewName");

        Mockito.when(playerService.updatePlayerById(eq(id), any(PlayerRequest.class)))
                .thenReturn(Mono.error(new ResourceNotFoundException("Not found")));

        webTestClient.put()
                .uri("/blackjack/players/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound();
    }

    // --- Delete Player Tests ---

    @Test
    void deletePlayer_Success() {
        Long id = 1L;
        Mockito.when(playerService.deletePlayerById(id)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/blackjack/players/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deletePlayer_Failure_InternalError() {
        Long id = 1L;
        // Simulating an unexpected database error
        Mockito.when(playerService.deletePlayerById(id))
                .thenReturn(Mono.error(new RuntimeException("DB Error")));

        webTestClient.delete()
                .uri("/blackjack/players/{id}", id)
                .exchange()
                .expectStatus().is5xxServerError();
    }

}

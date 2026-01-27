package com.example.JavaSprint5_1BlackJack.controllers;

import com.example.JavaSprint5_1BlackJack.DTO.GameRequest;
import com.example.JavaSprint5_1BlackJack.DTO.GameResponse;
import com.example.JavaSprint5_1BlackJack.DTO.PlayRequest;
import com.example.JavaSprint5_1BlackJack.services.GameService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/blackjack/game")
@AllArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GameResponse> createGame(
            @RequestBody @Valid GameRequest request
            ) {
        return gameService.createGame(request);
    }

    @GetMapping("/{id}")
    public Mono<GameResponse> findGameById(
            @PathVariable @NotBlank String id) {
        return gameService.findGameById(id);

    }

    @PostMapping("/{id}/play")
    public Mono<GameResponse> playGame(
            @PathVariable @NotBlank String id,
            @Valid @RequestBody PlayRequest request) {
        return gameService.playGame(id,request);

    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGameById(@PathVariable @NotBlank String id) {
        return gameService.deleteGameById(id);
    }



}

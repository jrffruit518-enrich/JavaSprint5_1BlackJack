package com.example.JavaSprint5_1BlackJack.controllers;

import com.example.JavaSprint5_1BlackJack.DTO.PlayerResponse;
import com.example.JavaSprint5_1BlackJack.DTO.PlayerRequest;
import com.example.JavaSprint5_1BlackJack.services.PlayerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/blackjack/players")
@AllArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PlayerResponse> createPlayer(
            @RequestBody
            @Valid
            PlayerRequest request) {
        return playerService.createPlayer(request);
    }

    @GetMapping("/{id}")
    public Mono<PlayerResponse> findPlayerById(
            @PathVariable Long id) {
        return playerService.findPlayerById(id);
    }

    @GetMapping
    public Flux<PlayerResponse> findAllPlayers() {
        return playerService.findAllPlayers();
    }

    @PutMapping("/{id}")
    public Mono<PlayerResponse> updatePlayerById(
            @PathVariable Long id,
            @RequestBody @Valid PlayerRequest request) {
       return playerService.updatePlayerById(id,request);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletePlayerById(
            @PathVariable Long id
    ) {
        return playerService.deletePlayerById(id);
    }

}

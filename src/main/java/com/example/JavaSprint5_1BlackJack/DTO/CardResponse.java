package com.example.JavaSprint5_1BlackJack.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record CardResponse(

        @Schema(description = "Suit of the card", example = "HEARTS", allowableValues = {"HEARTS", "DIAMONDS", "CLUBS", "SPADES"})
        String suit,

        @Schema(description = "Rank of the card", example = "ACE", allowableValues = {"TWO", "THREE", "...", "ACE"})
        String rank
) {
}

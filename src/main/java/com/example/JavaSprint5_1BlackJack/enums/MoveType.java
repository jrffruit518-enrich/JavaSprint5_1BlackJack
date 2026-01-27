package com.example.JavaSprint5_1BlackJack.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MoveType {
    HIT,   // Ask for another card
    STAND ; // Keep current hand and end player's turn

    @JsonCreator
    public static MoveType from(String value) {
        return MoveType.valueOf(value.toUpperCase());
    }
}

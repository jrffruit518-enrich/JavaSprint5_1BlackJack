package com.example.JavaSprint5_1BlackJack.entities;

import com.example.JavaSprint5_1BlackJack.enums.Rank;
import com.example.JavaSprint5_1BlackJack.enums.Suit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


@AllArgsConstructor
@Getter
public class Card {

    private final Suit suit;

    private final Rank rank;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    @Override
    public String toString() {
        return suit + " of " + rank;
    }
}

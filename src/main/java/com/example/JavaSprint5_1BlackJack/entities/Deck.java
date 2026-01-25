package com.example.JavaSprint5_1BlackJack.entities;

import com.example.JavaSprint5_1BlackJack.enums.Rank;
import com.example.JavaSprint5_1BlackJack.enums.Suit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deck {

    private  List<Card> cards = new ArrayList<>();

    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("The deck is empty! Cannot draw more cards.");
        }
        return cards.removeFirst();
    }

    public int remaining() {
        return cards.size();
    }

    public static Deck createNewShuffledDeck() {
        List<Card> cards = new ArrayList<>();
        for (Suit s:Suit.values()) {
            for (Rank r : Rank.values()) {
               cards.add(new Card(s,r));
            }
        }
        Collections.shuffle(cards);
        return new Deck(cards);
    }
}

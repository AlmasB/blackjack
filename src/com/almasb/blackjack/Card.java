package com.almasb.blackjack;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Game card
 *
 * Can be one of the 4 suits with value
 * ranging from 'Ace' to '2'
 *
 * @author Almas
 * @version 1.0
 */
public class Card extends Parent {

    enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    };

    enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(10), QUEEN(10), KING(10), ACE(11);

        final int value;
        private Rank(int value) {
            this.value = value;
        }
    };

    public final Suit suit;
    public final Rank rank;
    public final int value;

    /**
     * Card's "image"
     */
    private Rectangle rect = new Rectangle(80, 100);

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = rank.value;

        rect.setArcWidth(20);
        rect.setArcHeight(20);
        rect.setFill(Color.WHITE);

        Text text = new Text(toString());
        text.setWrappingWidth(70);

        StackPane stack = new StackPane();

        stack.getChildren().addAll(rect, text);

        getChildren().add(stack);
    }

    @Override
    public String toString() {
        return rank.toString() + " of " + suit.toString();
    }
}

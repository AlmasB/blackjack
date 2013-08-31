package uk.ac.brighton.uni.ab607.blackjack;

/**
 * Game card
 * 
 * Can be one of the 4 suits with value 
 * ranging from 'Ace' to '2'
 * 
 * @author Almas
 * @version 1.0
 */
public class Card {
    
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

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = rank.value;
    }
    
    @Override
    public String toString() {
        return rank.toString() + " of " + suit.toString();
    }
}

package com.almasb.blackjack;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Game card
 *
 * Can be one of the 4 suits with value
 * ranging from 'Ace' to '2'
 *
 * @author Almas
 */
public class Card extends Parent {

    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 120;
    

    enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES;

        final Image image;

        Suit() {
            this.image = new Image(Card.class.getResourceAsStream("images/".concat(name().toLowerCase()).concat(".png")),
                    32, 32, true, true);
        }
    }

    enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(10), QUEEN(10), KING(10), ACE(11);

        final int value;
        Rank(int value) {
            this.value = value;
        }

        String displayName() {
            return ordinal() < 9 ? String.valueOf(value) : name().substring(0, 1);
        }
    }

    public final Suit suit;
    public final Rank rank;
    public final int value;
    public final boolean flip;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = rank.value;
        this.flip = false;

        Rectangle bg = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        bg.setArcWidth(20);
        bg.setArcHeight(20);
        bg.setFill(Color.WHITE);

        Text text1 = new Text(rank.displayName());
        text1.setFont(Font.font(18));
        text1.setX(CARD_WIDTH - text1.getLayoutBounds().getWidth() - 10);
        text1.setY(text1.getLayoutBounds().getHeight());

        Text text2 = new Text(text1.getText());
        text2.setFont(Font.font(18));
        text2.setX(10);
        text2.setY(CARD_HEIGHT - 10);

        ImageView view = new ImageView(suit.image);
        view.setRotate(180);
        view.setX(CARD_WIDTH - 32);
        view.setY(CARD_HEIGHT - 32);

        getChildren().addAll(bg, new ImageView(suit.image), view, text1, text2);
    }
    /**
     * Something is wrong with the flip param, I might have to turn this into a method flipped and 
     * and 
     * @param suit
     * @param rank
     * @param flip
     */
    public Card(Suit suit, Rank rank, boolean flip) {
        this.suit = suit;
        this.rank = rank;
        this.value = rank.value;
        assert(flip == true);
        this.flip = flip;
        
        Rectangle bg = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        bg.setArcWidth(20);
        bg.setArcHeight(20);
        bg.setFill(Color.ROSYBROWN);
        
        Line line1 = new Line(CARD_WIDTH, CARD_HEIGHT,CARD_WIDTH, CARD_HEIGHT);
        Line line2 = new Line(CARD_WIDTH, CARD_HEIGHT,CARD_WIDTH, CARD_HEIGHT);
        
        line1.endXProperty().bind(bg.widthProperty().subtract(CARD_WIDTH-1));
        line1.endYProperty().bind(bg.heightProperty().subtract(CARD_HEIGHT-1));        
        line2.startXProperty().bind(bg.widthProperty().subtract(CARD_WIDTH-1));
        line2.endYProperty().bind(bg.heightProperty().subtract(CARD_HEIGHT-1));
        
        line1.setStroke(Color.GREEN);
        line1.setFill(Color.BLACK);
        line1.setStrokeWidth(3);
        
        line2.setStroke(Color.GREEN);
        line2.setFill(Color.BLACK);
        line2.setStrokeWidth(3);
        
        getChildren().addAll(bg,line1,line2);
        
    }

    @Override
    public String toString() {
        return rank.toString() + " of " + suit.toString();
    }
}

package uk.ac.brighton.uni.ab607.blackjack;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import uk.ac.brighton.uni.ab607.blackjack.View.DialogType;

/**
 * Inner model of the game
 * 
 * @author Almas
 * @version 1.0
 */
public class Model extends Observable implements Observer {
    
    private Deck deck = new Deck();;
    private Player player = new Player(deck);
    private Dealer dealer = new Dealer(deck);
    
    private int currentBet = 0;
    
    private ArrayList<Card> cardsOnTable = new ArrayList<Card>();
    
    public Model() {
        Controller.createInstance(this);
        player.addObserver(this);
        dealer.addObserver(this);
    }
    
    public void newGame() {
        if (player.getMoney() > 0) {
            player.reset();
            dealer.reset();
            deck.refill();
            cardsOnTable.clear();
            updateObservers(DialogType.ASK_BET);
        }
        else {
            updateObservers(DialogType.SHOW_NO_MONEY);
        }
    }

    public void run() {
        dealer.takeCard();
        dealer.takeCard();
        player.takeCard();
        playerHit();
    }
    
    public void playerHit() {
        player.takeCard();
        if (playable()) {
            updateObservers(DialogType.ASK_MOVE);
        }
        else {
            endGame();
        }
    }
    
    public void playerStand() {
        while (dealer.getValue() < 17) {
            dealer.takeCard();
        }
        endGame();
    }
    
    public boolean playerPlaceBet(int bet) {
        if (player.placeBet(bet)) {
            currentBet = bet;
            run();
            return true;
        }
        return false;
    }
    
    public boolean playable() {
        return player.getValue() < 21 && dealer.getValue() < 21;
    }
    
    public void endGame() {
        int p = player.getValue();
        int d = dealer.getValue();
          
        if (d == 21 || p > 21 || (d < 21 && d >= p) ) {
            updateObservers(DialogType.SHOW_LOSE);
        }
        else {
            player.win(2*currentBet);
            updateObservers(DialogType.SHOW_WIN);
        }  
        
    }
    
    public void updateObservers(Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof Card) {
            cardsOnTable.add((Card) arg);
            updateObservers(new ArrayList<Card>(cardsOnTable));
        }
    }
}

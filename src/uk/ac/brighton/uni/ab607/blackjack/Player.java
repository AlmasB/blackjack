package uk.ac.brighton.uni.ab607.blackjack;

public class Player extends Hand {

    public Player(Deck deck) {
        super(deck);
    }

    private int money = 100;
    
    public boolean placeBet(int bet) {
        if (bet > money) {
            return false;
        }
        
        money -= bet;
        return true;
    }
    
    public void win(final int money) {
        this.money += money;
    }
    
    public int getMoney() {
        return money;
    }
}

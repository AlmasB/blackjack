package uk.ac.brighton.uni.ab607.blackjack;

public class Main {

    public static void main(String[] args) {
       
        Model model = new Model();
        View view = new View();
        
        model.addObserver(view);
        Controller.getInstance().startNewGame();
    }
}

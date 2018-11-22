package com.almasb.blackjack;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Game's logic and UI
 *
 */
public class BlackjackApp extends Application {

    private Deck deck = new Deck();
    //private ObservableList<Hand> players; 
    private Hand dealer;
    private Text message = new Text();
    private int turn = 0, numPlayers = 2;
    private Hand[] players = new Hand[numPlayers]; 

    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private HBox dealerCards = new HBox(20);
    private HBox[] playerCards = new HBox[numPlayers]; 
    
    private Parent createContent() {
    
        dealer = new Hand(dealerCards.getChildren());
        
    	for(int i = 0; i < numPlayers; i++) {
    		playerCards[i] = new HBox(20);
    		//players.add(new Hand(playerCards[i].getChildren());
    		players[i] = new Hand(playerCards[i].getChildren());
    	}
    	//JAVAFX STUFF********************************
    	
        Pane root = new Pane();
        root.setPrefSize(800, 600);
        
		Region background = new Region();
        background.setPrefSize(800, 600);
        background.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");

        HBox rootLayout = new HBox(5);
        rootLayout.setPadding(new Insets(5, 5, 5, 5));
        Rectangle leftBG = new Rectangle(550, 560);
        leftBG.setArcWidth(50);
        leftBG.setArcHeight(50);
        leftBG.setFill(Color.GREEN);
        Rectangle rightBG = new Rectangle(230, 560);
        rightBG.setArcWidth(50);
        rightBG.setArcHeight(50);
        rightBG.setFill(Color.ORANGE);

        // LEFT
        VBox leftVBox = new VBox(10);
        leftVBox.setAlignment(Pos.TOP_CENTER);

        Text dealerScore = new Text("Dealer: ");
        leftVBox.getChildren().add(dealerScore);
        leftVBox.getChildren().add(dealerCards);
        leftVBox.getChildren().add(message);
        
        for(int i = 0; i < numPlayers; i++) {
        	HBox playersBox = new HBox(5, playerCards[i], new Text("Player "+ i + ":"));
        	playersBox.setAlignment(Pos.CENTER);
        	leftVBox.getChildren().add(playersBox);
        }
        
        
        // RIGHT

        VBox rightVBox = new VBox(20);
        rightVBox.setAlignment(Pos.CENTER);

        final TextField bet = new TextField("BET");
        bet.setDisable(true);
        bet.setMaxWidth(50);
        
        Text txtTurn  = new Text("Turn: ");
        Button btnPlay = new Button("PLAY");
        Button btnHit = new Button("HIT");
        
        Button btnStand = new Button("STAND");
        HBox buttonsHBox = new HBox(10, btnHit);
        buttonsHBox.setAlignment(Pos.CENTER);
        HBox standsHbox = new HBox(10, btnStand);
        standsHbox.setAlignment(Pos.CENTER);
        
        rightVBox.getChildren().addAll(txtTurn,bet, btnPlay, buttonsHBox, standsHbox);

        // ADD BOTH STACKS TO ROOT LAYOUT

        rootLayout.getChildren().addAll(new StackPane(leftBG, leftVBox), new StackPane(rightBG, rightVBox));
        root.getChildren().addAll(background, rootLayout);

        // BIND PROPERTIES

        btnPlay.disableProperty().bind(playable);
        btnHit.disableProperty().bind(playable.not());
        //btnHit2.disableProperty().bind(playable.not());
        btnStand.disableProperty().bind(playable.not());
        //btnStand2.disableProperty().bind(playable.not());

        //visuals
//        playerScore.textProperty().bind(new SimpleStringProperty("Player: ").concat(player.valueProperty().asString()));
//        player2Score.textProperty().bind(new SimpleStringProperty("Player2 : ").concat(player2.valueProperty().asString()));
   
        
        dealerScore.textProperty().bind(new SimpleStringProperty("Dealer: ").concat(dealer.valueProperty().asString()));
        

        
//        player.valueProperty().addListener((obs, old, newValue) -> {
//            if (newValue.intValue() >= 21) {
//            	if(turn >1) {
//            		endGame();
//            	}else {
//            		turn++;    
//            		txtTurn.textProperty().setValue("Turn: Player 2");
//            	}
//            }
//        });
//        player2.valueProperty().addListener((obs, old, newValue) -> {
//            if (newValue.intValue() >= 21) {
//            	if(turn >1) {
//            		endGame();
//            	}else {
//            		turn++;
//            	}
//            }
//        });

        dealer.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) {
                endGame();
            }
        });
        // INIT BUTTONS
        btnPlay.setOnAction(event -> {
        	txtTurn.textProperty().setValue("Turn: Player 0");
        	startNewGame();
        });

        btnHit.setOnAction(event -> {
        	if(turn >= numPlayers) {
        		btnStand.fire();
        	}
        	else {
        		int handVal = players[turn].valueProperty().get();
        	
        		if(handVal < 21) {
        			players[turn].takeCard(deck.drawCard());
        		}else {
        			turn++;
        			txtTurn.textProperty().setValue("Turn: Player " + turn);
        		}
        	}
    
        });

        btnStand.setOnAction(event -> {
    	turn++;
        	if(turn >= numPlayers) { 
        		txtTurn.textProperty().setValue("Dealer Turn");
        		while (dealer.valueProperty().get() < 17)
        			dealer.takeCard(deck.drawCard());
        	}else{
        			txtTurn.textProperty().setValue("Turn: Player" + turn);
        	}
        		endGame();
        });
        return root;
    }

    private void startNewGame() {
        playable.set(true);
        message.setText("");
        
        turn = 0;

        deck.refill();
        //Dealer 
        dealer.reset();
        dealer.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
        
        //Players
        for(int i = 0; i < numPlayers; i++) {
        	players[i].reset();
        	players[i].takeCard(deck.drawCard());
        	players[i].takeCard(deck.drawCard());
        }
    }

    private void endGame() {
        playable.set(false);
        ArrayList<String> winners = new ArrayList<String>();
        int dealerValue = dealer.valueProperty().get();
        int playerValue;
        
        String winner = "Exceptional case: \n d: " + dealerValue;
        
        for(int i =0; i < numPlayers; i++) {
        	playerValue = players[i].valueProperty().get();
        	
        	if (playerValue == 21 || dealerValue > 21 || playerValue > dealerValue) {
                winners.add("Player " + i);
        	}
        	
        }        
        message.setText("Winners against Dealer: " + winners.toString());
        turn = 0;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {    	
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("BlackJack");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

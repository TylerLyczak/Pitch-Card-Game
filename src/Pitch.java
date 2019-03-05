import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Pitch extends Application {
    int turn;
    boolean roundStart;
    boolean roundEnd;
    boolean roundMiddle;
    boolean roundBid;
    boolean botBid;
    Card roundCard;
    char trump;
    // Middle pile in the game. Decides trump and is cleared after turns are done
    ArrayList<Card> trickList;
    int amountOfPlayers;
    int playerWentFirst;
    int trickWinner;
    ArrayList<Character> suitsPlayed;


    Pitch ()    {
        trickList = new ArrayList<Card>();
        suitsPlayed = new ArrayList<Character>();
        roundStart = false;
        roundMiddle = false;
        roundEnd = false;
        roundBid = false;
        turn = 0;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public void setAmountOfPlayers (int num) { amountOfPlayers = num;}

    public void setRoundStart (boolean start)    { roundStart = start;}

    public void setRoundEnd (boolean end)   { roundEnd = end;}

    public void setRoundBid (boolean bidBool)   { roundBid = bidBool;}

    public void setTurn (int turnNum)   { turn = turnNum;}

    public boolean getRoundStart ()    { return roundStart;}

    public boolean getRoundEnd ()   { return roundEnd;}

    public boolean getRoundBid ()   { return roundBid;}

    public int getTurn ()  { return turn;}


    // Updates the HBox of the players hand with the cards
    public void updateHand (HBox hand, Player p1)   {
        System.out.println("Update Hand");
        for (int i=0; i<6; i++) {
            hand.getChildren().add(p1.hand.cards.get(i).cardButton);
            // if card is played, update button clickability
        }
        hand.setAlignment(Pos.BOTTOM_CENTER);
    }

    public FlowPane updateHand2 (FlowPane flow, Player p1)  {
        for (int i=0; i<6; i++) {
            flow.getChildren().add(p1.hand.cards.get(i).cardButton);
        }
        return flow;
    }

    // Updates the middle pile with the cards played by the players
    public void updateTickList (BorderPane gamePane, ArrayList<Card> trickList)   {
        //FlowPane trickFlow = new FlowPane();
        StackPane trickPane = new StackPane();
        //gamePane.setCenter(null);
        for (int i=0; i<trickList.size(); i++)  {
            //flow.getChildren().add(trickList.get(i).cardPic);
            System.out.println("In Trick, card: " + trickList.get(i).src);
            trickList.get(i).cardPic.setRotate(50*i);
            trickPane.getChildren().add(trickList.get(i).cardPic);
        }
        gamePane.setCenter(trickPane);

        /*
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        //gamePane.setLeft(trickPane);
    }

    // After turns are completed, the cards in the middle are removed.
    public void removeTrickList (Player p1, BorderPane gamePane)    {

        //gamePane.setCenter(null);
        trickList.clear();
        p1.changeBoolButtonPress2();
        gamePane.setCenter(null);
    }

    public void decideTurn ()   {

    }

    public void decideTrump ()  {
        if (roundStart && trickList.size() != 0) {
            trump = trickList.get(0).suit;
            System.out.println("Trump: " + trump);
            roundStart = false;
            roundMiddle = true;
        }
    }

    // Determines the highest played card of the current trick with trump played or not
    public int decideTrick ()  {
        if (amountOfPlayers != trickList.size())    { return 0;}

        boolean trumpPlayed = false;
        ArrayList<Card> trumpCards = new ArrayList<Card>();
        for (int i=0; i<trickList.size(); i++)  {
            if (trickList.get(i).getSuit() == trump)    {
                trumpPlayed = true;
                trumpCards.add(trickList.get(i));
            }
        }

        if (trumpCards.size() == 1) { return trumpCards.get(0).getPlayedBy();}
        else if (trumpCards.size() > 1)  {
            Card highCard = trumpCards.get(0);
            for (int i=0; i<trumpCards.size(); i++) {
                if (trumpCards.get(i).getPoints() > highCard.getPoints())   {
                    highCard = trumpCards.get(i);
                }
            }
            return highCard.getPlayedBy();
        }
        // No trumps were played in current trick
        else {
            Card highCard = trickList.get(0);
            for (int i = 0; i < trickList.size(); i++) {
                if (trickList.get(i).getPoints() > highCard.getPoints()) {
                    highCard = trickList.get(i);
                }
            }
            return highCard.getPlayedBy();
        }
    }

    // Gives the winner of that turn the trick
    public int playerReceiveTrick (Player p1, ArrayList<AIPlayer> AI, BorderPane gamePane)   {
        int winner = decideTrick();

        // Human player case
        if (winner == 1)    {
            for (int i=0; i<trickList.size(); i++)  {
                p1.getTrickDeck().add(trickList.get(i));
            }
        }
        // Bot case
        else {
            for (int i=0; i<trickList.size(); i++)  {
                 AI.get(winner-2).getTrickDeck().add(trickList.get(i));
            }
        }


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        removeTrickList(p1, gamePane);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        //System.out.println("Winner: " + winner);
        trickWinner = winner;
        return winner;
    }

    public boolean checkRoundEnd (Player p1, ArrayList<AIPlayer> AI)   {
        boolean check = false;
        if (p1.getHand().getCards().size() == 0)    {
            check = true;
            System.out.println("P1 check true");
        }
        else    { return false;}

        for (int i=0; i<amountOfPlayers-1; i++) {
            if (AI.get(i).getHand().getCards().size() == 0)    {
                check = true;
                System.out.println("AI num: " + i + "check true");
            }
            else    {
                check = false;
                System.out.println("AI num: " + i + " hand size: " + AI.get(i).getHand().getCards().size());
                return false;
            }
        }

        if (check)  {
            //calc score
            System.out.println("Check Success");
            roundMiddle = false;
            roundEnd = true;
            return true;
        }
        else    { return false;}
    }

    // Calculates the final score of each players trick with there bids
    public void calculateScore (Player p1, ArrayList<AIPlayer> AI)  {

        // Calculate game point
        // For player
        ArrayList<Integer> gamePointList = new ArrayList<Integer>();
        if (p1.getTrickDeck().size() > 0)   {
            int amount = 0;
            for (int i=0; i<p1.getTrickDeck().size(); i++)  {
                amount += p1.getTrickDeck().get(i).getPoints();
            }
            gamePointList.add(amount);
        }
        else    {
            gamePointList.add(0);
        }

        // For bot
        for (int i=0; i<AI.size(); i++) {
            int botAmount = 0;
            for (int j=0; j<AI.get(i).getTrickDeck().size(); j++)   {
                botAmount += AI.get(i).getTrickDeck().get(j).getPoints();
            }
            gamePointList.add(botAmount);
        }

        // Calculates who gets the game point
        // This test if two players got the game point
        int highPoint = gamePointList.get(0);
        int highIndex = 0;
        boolean twoWinners = false;
        for (int i=0; i<gamePointList.size(); i++)  {
            if (gamePointList.get(i) > highPoint)   {
                highPoint = gamePointList.get(i);
                highIndex = i;
            }
        }
        for (int i=0; i<gamePointList.size(); i++)  {
            if (highIndex == i) {
                continue;
            }
            else if (highPoint == gamePointList.get(i)) {
                twoWinners = true;
                break;
            }
        }
        int gamePointPlayer = 5;
        if (!twoWinners) {
            gamePointPlayer = highIndex;
        }


        // High / Low / Jack of Trumps
        Card high = null;
        Card low = null;
        // Sets to inital value of 5 to make sure it doesn't give any player the point if not found, like jack of trump
        int playerHigh = 5;
        int playerLow = 5;
        int playerJack = 5;
        // For player
        // Checks size of the trick deck
        if (p1.getTrickDeck().size() > 0)   {
            for (int i=0; i<p1.getTrickDeck().size(); i++)  {
                // Finds which cards are trump
                if (p1.getTrickDeck().get(i).getSuit() == trump)    {
                    // If the first trump card is found, it will be the high
                    if (high == null)   {
                        high = p1.getTrickDeck().get(i);
                        playerHigh = 0;
                    }
                    // Compares any trump card to the high trump card, returns true if high is the lower value
                    else if (Card.cardSuitDecider (high.getRank(), p1.getTrickDeck().get(i).getRank()))  {
                        high = p1.getTrickDeck().get(i);
                    }

                    // If the first trump card is found, it will be the low
                    if (low == null)    {
                        low = p1.getTrickDeck().get(i);
                        playerLow = 0;
                    }
                    // Compares any trump card to the low trump card, returns true if low is the higher value
                    else if (Card.cardSuitDecider(p1.getTrickDeck().get(i).getRank(), low.getRank()))   {
                        low = p1.getTrickDeck().get(i);
                        playerLow = 0;
                    }

                    // If the jack is found, then it will set it to the player with it
                    if (p1.getTrickDeck().get(i).getRank().equals("J")) {
                        playerJack = 0;
                    }
                }
            }
        }

        // For bots
        for (int i=0; i<AI.size(); i++) {
            // Checks size of the trick deck
            if (AI.get(i).getTrickDeck().size() > 0)    {
                for (int j=0; j<AI.get(i).getTrickDeck().size(); j++)   {
                    // Finds which cards are trump
                    if (AI.get(i).getTrickDeck().get(j).getSuit() == trump) {
                        // If the first trump card is found, it will be the high
                        if (high == null)   {
                            high = AI.get(i).getTrickDeck().get(j);
                            playerHigh = i+1;
                        }
                        // Compares any trump card to the high trump card, returns true if high is the lower value
                        else if (Card.cardSuitDecider(high.getRank(), AI.get(i).getTrickDeck().get(j).getRank()))    {
                            high = AI.get(i).getTrickDeck().get(j);
                            playerHigh = i+1;
                        }

                        // If the first trump card is found, it will be the low
                        if (low == null)    {
                            low = AI.get(i).getTrickDeck().get(i);
                            playerLow = i+1;
                        }
                        // Compares any trump card to the low trump card, returns true if low is the higher value
                        else if (Card.cardSuitDecider(AI.get(i).getTrickDeck().get(i).getRank(), low.getRank()))   {
                            low = AI.get(i).getTrickDeck().get(i);
                            playerLow = i+1;
                        }

                        // If the jack is found, then it will set it to the player with it
                        if (AI.get(i).getTrickDeck().get(i).getRank().equals("J")) {
                            playerJack = i+1;
                        }
                    }
                }
            }
        }

        // Gives the point to each player, incrementing there temp points
        if (gamePointPlayer == 5)   { /* No-one gets the point*/}
        else if (gamePointPlayer == 0)   {
            p1.incrementTempPoints(1);
        }
        else    {
            AI.get(gamePointPlayer-1).incrementTempPoints(1);
        }

        if (playerHigh == 0) {
            p1.incrementTempPoints(1);
        }
        else    { AI.get(playerHigh-1).incrementTempPoints(1);}

        if (playerLow == 0) {
            p1.incrementTempPoints(1);
        }
        else    { AI.get(playerLow-1).incrementTempPoints(1);}

        if (playerJack == 5)    { /* No-one gets the point*/}
        else if (playerJack == 0)   {
            p1.incrementTempPoints(1);
        }
        else if (playerJack > 0 && playerJack < 5)  {
            AI.get(playerJack-1).incrementTempPoints(1);
        }

        // Calculates if the players got greater than or equal to their bid
        if (p1.getTempPoints() < p1.getBid())   {
            p1.decrementPoints(p1.getBid());
        }
        else    {
            p1.incrementPoints(p1.getBid());
        }
        for (int i=0; i<AI.size(); i++) {
            if (AI.get(i).getTempPoints() < AI.get(i).getBid()) {
                AI.get(i).decrementPoints(AI.get(i).getBid());
            }
            else    {
                AI.get(i).incrementPoints(AI.get(i).getTempPoints());
            }
        }
        // Resets all the temp points used to calculate there score
        p1.resetTempPoints();
        for (int i=0; i<AI.size(); i++) {
            AI.get(i).resetTempPoints();
        }


        System.out.println("Person Score: " + p1.points);

        for (int i=0; i<AI.size(); i++) {
            System.out.println("Bot " + i + " Score: " + AI.get(i).points);
        }
    }

    public int determineFirstTurn (Player p1, ArrayList<AIPlayer> AI)   {
        int highPlayer = 1;
        int highestBid = 0;
        highestBid = p1.getBid();

        for (int i=0; i<AI.size(); i++) {
            if (AI.get(i).getBid() > highestBid)    {
                highestBid = AI.get(i).getBid();
                highPlayer = i+2;
            }
        }

        return highPlayer;

    }

    public boolean gameTurn (Player p1, ArrayList<AIPlayer> AI, BorderPane gamePane)    {
        if (!roundMiddle)   { return false;}
        if (turn == 1)  {
            if (p1.playCard(trickList)) {
                updateTickList(gamePane, trickList);
                updateSuitsPlayed();
                turn = 2;
                return true;
            }
            else    { return false;}
        }
        else if (turn == 2) {
            AI.get(0).playCard(trickList, 2);
            updateTickList(gamePane, trickList);
            updateSuitsPlayed();
            if (AI.size() == 1) {
                turn = 1;
                return true;
            }
            else {
                turn = 3;
                return true;
            }
        }
        else if (turn == 3) {
            AI.get(1).playCard(trickList, 3);
            updateTickList(gamePane, trickList);
            updateSuitsPlayed();
            if (AI.size() == 2) { turn = 1; return true;}
            else { turn = 4; return true;}
        }
        else if (turn == 4) {
            AI.get(2).playCard(trickList, 4);
            updateTickList(gamePane, trickList);
            updateSuitsPlayed();
            turn = 1;
            return true;
        }
        return false;
    }

    // Adds any suit that is played into the list
    public void updateSuitsPlayed ()    {
        for (int i=0; i<trickList.size(); i++)    {
            if (!suitsPlayed.contains(trickList.get(i).getSuit()))  {
                suitsPlayed.add(trickList.get(i).getSuit());
            }
        }
    }

    // Clears the suit array list
    public void clearSuitsPlayed () { suitsPlayed.clear();}

    public void addTrumpSuitsPlayed ()  {suitsPlayed.add(trump);}

    // Changes the players hand to the suits that were played
    public void updatePlayerHand (Player p1) {
        boolean changed = false;
        p1.changeCardDisability(true);
        for (int i=0; i<p1.getHand().getCards().size(); i++)    {
            for (int j=0; j<suitsPlayed.size(); j++)    {
                if (p1.getHand().getCards().get(i).getSuit() == suitsPlayed.get(j)) {
                    p1.getHand().getCards().get(i).getCardButton().setDisable(false);
                    changed = true;
                }
            }
        }
        // If the player has no cards of the suits played, the player can play any card
        if (!changed)   { p1.changeCardDisability(false);}

        if (trickWinner == 1)    { p1.changeCardDisability(false);}
    }

    // Checks if any player reached 7 or above points
    public boolean isWinner (Player p1, ArrayList<AIPlayer> AI) {
        if (p1.getPoints() >= 7)    {
            return true;
        }
        for (int i=0; i<AI.size(); i++) {
            if (AI.get(i).getPoints() >= 7) {
                return true;
            }
        }
        return false;
    }

    // Adds all the players above 7 points to an array list
    public ArrayList<Integer> determineWinner (Player p1, ArrayList<AIPlayer> AI)  {
        ArrayList<Integer> winners = new ArrayList<Integer>();
        if (p1.getPoints() >= 7)    {
            winners.add(1);
        }
        for (int i=0; i<AI.size(); i++) {
            if (AI.get(i).getPoints() >= 7) {
                winners.add(i+2);
            }
        }
        return winners;
    }

    public Scene makeWinnerScene (ArrayList<Integer> winners, HashMap<String, Scene> sceneMap, Stage myStage, Button exitWinner)   {
        Text t = new Text();
        t.setFont(new Font(50));
        t.setWrappingWidth(500);
        t.setTextAlignment(TextAlignment.CENTER);

        String winnerText = "Congratulations to Player " + winners.get(0);
        for (int i=1; i<winners.size(); i++)    {
            winnerText += " And Player " + winners.get(i);
        }
        winnerText += "!";
        t.setText(winnerText);

        HBox winnerHbox = new HBox(10, t);
        winnerHbox.setAlignment(Pos.CENTER);
        VBox winnerFinal = new VBox(20, winnerHbox, exitWinner);
        winnerFinal.setAlignment(Pos.CENTER);


        Scene winnerScene = new Scene(winnerFinal, 800, 800);


        return winnerScene;
    }



}






















































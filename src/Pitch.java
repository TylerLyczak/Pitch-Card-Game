import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Pitch extends Application {
    int turn;
    boolean roundStart;
    boolean roundEnd;
    boolean roundMiddle;
    Card roundCard;
    char trump;
    // Middle pile in the game. Decides trump and is cleared after turns are done
    ArrayList<Card> trickList;
    int amountOfPlayers;
    int playerWentFirst;


    Pitch ()    {
        trickList = new ArrayList<Card>();
        roundStart = false;
        roundMiddle = false;
        roundEnd = false;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public void setAmountOfPlayers (int num) { amountOfPlayers = num;}

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

    public void roundStart ()   {
        String trump;

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

    public void playerReceiveTrick (Player p1, ArrayList<AIPlayer> AI, BorderPane gamePane)   {
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
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        removeTrickList(p1, gamePane);
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
        int gamePointPlayer = 0;
        for (int i=0; i<gamePointList.size(); i++)  {
            if (gamePointList.get(i) > gamePointList.get(gamePointPlayer))  {
                gamePointPlayer = i;
            }
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

        // Gives the points to each player
        // FIXXXXXXXX
        // Make it work with bidding
        if (gamePointPlayer == 0)   {
            p1.incrementPoints(1);
        }
        else    {
            AI.get(gamePointPlayer-1).incrementPoints(1);
        }

        if (playerHigh == 0) {
            p1.incrementPoints(1);
        }
        else    { AI.get(playerHigh-1).incrementPoints(1);}

        if (playerLow == 0) {
            p1.incrementPoints(1);
        }
        else    { AI.get(playerLow-1).incrementPoints(1);}

        if (playerJack == 5)    {
            // Nothing
        }
        else if (playerJack == 0)   {
            p1.incrementPoints(1);
        }
        else if (playerJack > 0 && playerJack < 5)  {
            AI.get(playerJack-1).incrementPoints(1);
        }

        System.out.println("Person Score: " + p1.points);

        for (int i=0; i<AI.size(); i++) {
            System.out.println("Bot " + i + " Score: " + AI.get(i).points);
        }
    }



}






















































import javafx.application.Application;
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
    public HBox updateHand (HBox hand, Player p1)   {
        System.out.println("Update Hand");
        for (int i=0; i<6; i++) {
            hand.getChildren().add(p1.hand.cards.get(i).cardButton);
            // if card is played, update button clickability
        }
        return hand;
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

    public void calculateScore (Player p1, ArrayList<AIPlayer> AI)  {
        if (p1.getHand().getCards().size() == 0)    {

        }
    }

}


























import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class Trick {
    Deck trickDeck;
    int turn;
    int bid;
    int cardNum;
    ArrayList<Card> trickTest;

    Trick ()    {
        trickDeck = new Deck();
        trickTest = new ArrayList<Card>();
    }

    public void addCard (Card c1)  {
        trickTest.add(c1);
    }

    public void displayCards () {
        if (trickTest.size() == 1)  {

        }
    }

}

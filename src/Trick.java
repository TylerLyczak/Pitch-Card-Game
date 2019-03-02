import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class Trick {
    Deck trickDeck;
    int turn;
    int bid;
    int cardNum;
    ArrayList<Card> trickList;

    Trick ()    {
        trickDeck = new Deck();
        trickList = new ArrayList<Card>();
    }

    public void addCard (Card c1)  {
        trickList.add(c1);
    }

    public void displayCards () {
        if (trickList.size() == 1)  {

        }
    }

    public void updateTickList (BorderPane gamePane, ArrayList<Card> tr)   {
        //FlowPane trickFlow = new FlowPane();
        StackPane trickPane = new StackPane();
        //gamePane.setCenter(null);
        for (int i=0; i<tr.size(); i++)  {
            //flow.getChildren().add(trickList.get(i).cardPic);
            System.out.println("In Trick, card: " + tr.get(i).src);
            tr.get(i).cardPic.setRotate(50*i);
            trickPane.getChildren().add(tr.get(i).cardPic);
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

    public void removeTrickList (BorderPane gamePane, ArrayList<Card> tr, Player p1)    {

        //gamePane.setCenter(null);
        tr.clear();
        p1.changeBoolButtonPress2();
        //gamePane.setCenter(null);
    }

}

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


    @Override
    public void start(Stage primaryStage) throws Exception {

    }

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

    public void removeTrickList (BorderPane gamePane, ArrayList<Card> trickList, Player p1)    {

        //gamePane.setCenter(null);
        trickList.clear();
        p1.changeBoolButtonPress2();
        //gamePane.setCenter(null);
    }

}

import javafx.application.Application;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Pitch extends Application {
    int turn;

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public HBox updateHand (HBox hand, Player p1)   {
        System.out.println("Update Hand");
        for (int i=0; i<6; i++) {
            hand.getChildren().add(p1.hand.cards.get(i).cardButton);
        }
        return hand;
    }

    public FlowPane updateHand2 (FlowPane flow, Player p1)  {
        for (int i=0; i<6; i++) {
            flow.getChildren().add(p1.hand.cards.get(i).cardButton);
        }
        return flow;
    }

}

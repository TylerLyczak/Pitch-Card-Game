import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;

public class Player {
    Deck hand;
    ArrayList<Deck> trickList;
    int bid;
    boolean buttonPress;

    Player ()   {
        hand = new Deck();
        trickList = new ArrayList<Deck>();
        buttonPress = false;

    }

    public void makeBid ()  {

    }

    public boolean playCard (ArrayList<Card> trick) {
        //boolean pressed = false;
        for (int i=0; i<this.hand.cards.size(); i++)    {
            //System.out.println(hand.cards.get(i).cardButton);
            hand.cards.get(i).cardButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for (int i=0; i<hand.cards.size(); i++) {
                        if (hand.cards.get(i).cardButton == event.getSource())  {
                            System.out.println("YEEET");
                            //Update hbox
                            hand.cards.get(i).cardButton.setVisible(false);
                            trick.add(hand.cards.get(i));
                            hand.cards.remove(i);
                            buttonPress = true;
                            System.out.println(buttonPress);
                            //return c1;
                            break;
                        }
                    }

                    //System.out.println(event.getSource());
                    //hand.cards.remove(event);
                }
            });
        }
        //System.out.println("Returning: " + buttonPress);
        return buttonPress;
    }

    public void changeBoolButtonPress ()    {
        buttonPress = false;
    }

    public static void actionPerformed (javafx.event.ActionEvent event) {
        System.out.println("wowwwwww");
        System.out.println(event.getEventType());
        //System.out.println(event.);

    }


}

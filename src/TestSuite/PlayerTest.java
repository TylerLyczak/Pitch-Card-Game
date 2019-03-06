import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PlayerTest{

    Player p1 = new Player();

    @BeforeAll
    static void startMessage () {
        System.out.println("Starting Test...");
    }

    @AfterAll
    static void endMessage ()   {
        System.out.println("Ending Test");
    }

    @Test
    void testConstructor()   {
        Player newPl = new Player();
        assertNotEquals(null, p1.getHand(), "Testing if Player constructor was called");
    }

    @Test
    void getBid() {
        assertEquals(0, p1.getBid(), "Testing if player has bid of 0");
    }

    @Test
    void setBid ()  {
        p1.setBid(10);
        assertEquals(10, p1.getBid(), "Testing if players bid was set");
    }

    @Test
    void resetBid() {
        p1.setBid(2);
        p1.resetBid();
        assertEquals(0, p1.getBid(), "Testing is player's bid was reset");
    }

    @Test
    void getHand () {
        assertNotEquals(null, p1.getHand());
    }

    @Test
    void changeCardDisability (){
        new JFXPanel();
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        p1.getHand().addCard(c1);
        p1.changeCardDisability(true);
        assertEquals(true, p1.getHand().getCards().get(0).getCardButton().isDisabled());
    }

    @Test
    void playCard ()    {
        new JFXPanel();
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        p1.getHand().addAllCards();
        ArrayList<Card> trick = new ArrayList<Card>();
        trick.add(c1);
        boolean testBool = p1.playCard(trick);
        assertEquals(false, testBool);
    }



}
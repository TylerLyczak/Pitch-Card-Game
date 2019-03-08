import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PitchTest {

    Pitch testPitch = new Pitch();

    @BeforeAll
    static void startMessage () {
        new JFXPanel();
        System.out.println("Starting Test...");
    }

    @AfterAll
    static void endMessage ()   {
        System.out.println("Ending Test");
    }

    @Test
    void testConstructor () {
        assertNotEquals(null, testPitch, "Tests if Pitch was created");
        assertNotEquals(null, testPitch.getRoundEnd(), "Test is a value was initialized in Pitch()");
    }

    @Test
    void updateHand() {
        HBox testBox = new HBox();
        Player testPlayer = new Player();
        PitchDealer testDealer = new PitchDealer();
        testDealer.createDealer();
        testPlayer.getHand().setCards(testDealer.dealHand());
        testPitch.updateHand(testBox, testPlayer);

        HBox compareBox = new HBox();
        Player comparePlayer = new Player();
        testDealer.createDealer();
        comparePlayer.getHand().setCards(testDealer.dealHand());
        for (int i=0; i<6; i++) {
            compareBox.getChildren().add(comparePlayer.getHand().getCards().get(i).getCardButton());
        }
        //compareBox.getChildren().size();
        assertEquals(compareBox.getChildren().size(), testBox.getChildren().size(), "Tests if the two HBoxes are the same");
    }

    @Test
    void determineFirstTurn() {
        // Sets up player and AI with bids
        // Player 3 (testBot2) is the highest bidder and should be returned
        Player testPlayer = new Player();
        testPlayer.setBid(2);
        AIPlayer testBot1 = new AIPlayer();
        testBot1.setBid(3);
        AIPlayer testBot2 = new AIPlayer();
        testBot2.setBid(4);
        AIPlayer testBot3 = new AIPlayer();
        testBot3.setBid(1);
        ArrayList<AIPlayer> AI = new ArrayList<AIPlayer>();
        AI.add(testBot1);
        AI.add(testBot2);
        AI.add(testBot3);
        assertEquals(3, testPitch.determineFirstTurn(testPlayer, AI), "Tests if the right player is returned");
    }

    @Test
    void updateSuitsPlayed() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2, 2);
        ArrayList<Card> testTrick = new ArrayList<Card>();
        testTrick.add(c1);
        testPitch.setTrickList(testTrick);
        testPitch.updateSuitsPlayed();
        assertEquals(1, testPitch.getSuitsPlayed().size(), "Tests if suitsPlayed was updated");
    }

    @Test
    void clearSuitsPlayed() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2, 2);
        ArrayList<Card> testTrick = new ArrayList<Card>();
        testTrick.add(c1);
        testPitch.setTrickList(testTrick);
        testPitch.updateSuitsPlayed();
        testPitch.clearSuitsPlayed();
        assertEquals(0, testPitch.getSuitsPlayed().size(), "Tests if suitsPlayed was cleared");
    }
}
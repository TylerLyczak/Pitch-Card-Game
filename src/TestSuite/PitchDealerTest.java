
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PitchDealerTest {

    PitchDealer dealer = new PitchDealer();

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
    void createDealer() {
        dealer.createDealer();
        assertNotEquals(0, dealer.getGameDealer().gameDeck.getCards().size(), "Tests if dealer was created");
    }

    @Test
    void dealHand() {
        dealer.createDealer();
        ArrayList<Card> hand = dealer.dealHand();
        assertEquals(6, hand.size(), "Tests if it returns 6 cards");
    }

    @Test
    void dealerReset() {
        dealer.createDealer();
        Deck newDeck = new Deck();
        newDeck.addAllCards();
        dealer.dealerReset();
        assertNotEquals(newDeck.getCards().get(0).getRank(), dealer.getGameDealer().gameDeck.getCards().get(0).getRank(),
                "Testing if they have the same order which they shouldn't");
    }

    @Test
    void checkDeck() {
        dealer.createDealer();
        dealer.dealerReset();
        assertEquals(52, dealer.getGameDealer().gameDeck.getCards().size(), "Tests if deck was deleted");
    }

    @Test
    void setGameStart() {
        dealer.createDealer();
        dealer.setGameStart(true);
        assertEquals(true, dealer.getGameStart(), "Tests if gameStart was changed");
    }
}
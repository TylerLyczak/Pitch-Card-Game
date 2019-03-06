import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CardTest {

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
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        assertNotEquals (null, c1);
    }

    @Test
    void setPlayedBy() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        c1.setPlayedBy(1);
        assertEquals(1, c1.getPlayedBy(), "Tests if the playedBy value is changed");
    }

    @Test
    void getPoints() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        assertEquals(2, c1.getPoints(), "Tests if getPoints returns the right points");
    }

    @Test
    void getSuit() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        assertEquals('C', c1.getSuit(), "Tests if getSuit returns the right suit");
    }

    @Test
    void getPlayedBy() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        c1.setPlayedBy(1);
        assertEquals(1, c1.getPlayedBy(), "Tests if the playedBy value is changed");
    }

    @Test
    void getRank() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        assertEquals("2", c1.getRank(), "Tests the getRank returns the right value");
    }

    @Test
    void getCardButton() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        assertNotEquals(null, c1.getCardButton(), "Tests is getCardButton returns a button");
    }

    @Test
    void cardValue() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        assertEquals(0, Card.cardValue(c1.getRank()), "Tests the value of a card");
    }

    @Test
    void cardRank() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        assertEquals(2, Card.cardRank(c1.getRank()), "Tests the ranks of a card");
    }

    @Test
    void cardSuitDecider() {
        Card c1 = new Card("2", 'C', "file:../playingcards/2C.png", 2);
        Card c2 = new Card("A", 'C', "file:../playingcards/AC.png", 2);
        assertEquals(false, Card.cardSuitDecider(c2.getRank(), c1.getRank()),
                "Tests if card rank is bigger than an other card");
        assertEquals(false, Card.cardSuitDecider(c1.getRank(), c1.getRank()),
                "Tests id the same card is compared to itself");
    }
}
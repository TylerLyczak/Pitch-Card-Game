import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AIPlayerTest {

    AIPlayer testBot;

    @BeforeAll
    static void startMessage () {
        new JFXPanel();
        System.out.println("Starting Test...");
    }

    @BeforeEach
    void init ()    {
        testBot = new AIPlayer();
    }

    @AfterAll
    static void endMessage ()   {
        System.out.println("Ending Test");
    }

    @Test
    void testConstructor () {
        assertNotEquals(null, testBot.getRandomGenerator(), "Test if randomGenerator was initialized");
        assertNotEquals(null, testBot.getHand(), "Test if hand was initialized");
        assertNotEquals(null, testBot.getTrickDeck(), "Tests if trickDeck was initialized");
    }

    // This test will see if updateCardHandWeights() returns a weight value of 1 for spades when the whole hand is spades
    @Test
    void updateCardHandWeightsTest1 ()   {
        Card c1 = new Card("J", 'S', "file:../playingcards/JS.png", 11, 11);
        Card c2 = new Card("3", 'S', "file:../playingcards/3S.png", 3, 11);
        Card c3 = new Card("Q", 'S', "file:../playingcards/QS.png", 12, 12);
        Card c4 = new Card("5", 'S', "file:../playingcards/5S.png", 5, 5);
        Card c5 = new Card("10", 'S', "file:../playingcards/10S.png", 10, 10);
        testBot.getHand().addCard(c1);
        testBot.getHand().addCard(c2);
        testBot.getHand().addCard(c3);
        testBot.getHand().addCard(c4);
        testBot.getHand().addCard(c5);
        testBot.updateCardHandWeights();
        assertEquals(1, testBot.getSpadeWeight());
    }

    // This test will see if updateCardHandWeights() returns a weight value of .25 for spades when
    // there is one of each suit in the hand
    @Test
    void updateCardHandWeightsTest2 ()  {
        Card c1 = new Card("A", 'S', "file:../playingcards/AS.png", 14, 14);
        Card c2 = new Card("A", 'D', "file:../playingcards/AD.png", 14, 14);
        Card c3 = new Card("A", 'H', "file:../playingcards/AH.png", 14, 14);
        Card c4 = new Card("A", 'C', "file:../playingcards/AC.png", 14, 14);
        testBot.getHand().addCard(c1);
        testBot.getHand().addCard(c2);
        testBot.getHand().addCard(c3);
        testBot.getHand().addCard(c4);
        testBot.updateCardHandWeights();
        assertEquals(.25, testBot.getSpadeWeight());
    }

    // Case 1.1 is where no cards were played at all, so new round
    // Bot will play its highest suit card
    @Test
    void playCardCase1Test1() {
        char trump = 'S';
        ArrayList<Character> suits = new ArrayList<Character>();
        ArrayList<Card> trickList = new ArrayList<Card>();
        Card c1 = new Card("J", 'S', "file:../playingcards/JS.png", 11, 11);
        Card c2 = new Card("3", 'S', "file:../playingcards/3S.png", 3, 3);
        Card c3 = new Card("Q", 'S', "file:../playingcards/QS.png", 12, 12);
        Card c4 = new Card("5", 'S', "file:../playingcards/5S.png", 5, 5);
        Card c5 = new Card("10", 'S', "file:../playingcards/10S.png", 10, 10);
        testBot.getHand().addCard(c1);
        testBot.getHand().addCard(c2);
        testBot.getHand().addCard(c3);
        testBot.getHand().addCard(c4);
        testBot.getHand().addCard(c5);
        ArrayList<Card> originalHand = (ArrayList<Card>)testBot.getHand().getCards().clone();
        testBot.playCard(trickList, suits, 1, trump);
        originalHand.removeAll(testBot.getHand().getCards());
        assertEquals(c3.getRank(), originalHand.get(0).getRank(), "Test first case, should return Queen");
    }

    // Case 1.1 is where no cards were played at all, so new round
    // Bot will play its highest suit card of its most weighted suit, which is the Queen of Spades
    @Test
    void playCardCase1Test2 ()   {
        char trump = 'S';
        ArrayList<Character> suits = new ArrayList<Character>();
        ArrayList<Card> trickList = new ArrayList<Card>();
        Card c1 = new Card("J", 'S', "file:../playingcards/JS.png", 11, 11);
        Card c2 = new Card("3", 'S', "file:../playingcards/3S.png", 3, 3);
        Card c3 = new Card("Q", 'S', "file:../playingcards/QS.png", 12, 12);
        Card c4 = new Card("A", 'C', "file:../playingcards/AC.png", 5, 5);
        Card c5 = new Card("10", 'S', "file:../playingcards/10S.png", 10, 10);
        testBot.getHand().addCard(c1);
        testBot.getHand().addCard(c2);
        testBot.getHand().addCard(c3);
        testBot.getHand().addCard(c4);
        testBot.getHand().addCard(c5);
        ArrayList<Card> originalHand = (ArrayList<Card>)testBot.getHand().getCards().clone();
        testBot.playCard(trickList, suits, 1, trump);
        originalHand.removeAll(testBot.getHand().getCards());
        assertEquals(c3.getRank(), originalHand.get(0).getRank(), "Test first case, should return Queen of Spades");
    }

    // Case 1.2
    @Test
    void playCardCase1Test3 ()  {
        char trump = 'S';
        ArrayList<Character> suits = new ArrayList<Character>();
        ArrayList<Card> trickList = new ArrayList<Card>();
        Card c1 = new Card("4", 'S', "file:../playingcards/4S.png", 4, 4);
        Card c2 = new Card("3", 'S', "file:../playingcards/3S.png", 3, 3);
        Card c3 = new Card("6", 'S', "file:../playingcards/6S.png", 6, 6);
        Card c4 = new Card("8", 'S', "file:../playingcards/8S.png", 8, 8);
        Card c5 = new Card("10", 'S', "file:../playingcards/10S.png", 10, 10);
        testBot.getHand().addCard(c1);
        testBot.getHand().addCard(c2);
        testBot.getHand().addCard(c3);
        testBot.getHand().addCard(c4);
        testBot.getHand().addCard(c5);
        ArrayList<Card> originalHand = (ArrayList<Card>)testBot.getHand().getCards().clone();
        testBot.playCard(trickList, suits, 1, trump);
        originalHand.removeAll(testBot.getHand().getCards());
        assertEquals(c2.getRank(), originalHand.get(0).getRank(), "Test first case, should return 3 of Spades");
    }

    @Test
    void determineBid() {
    }
}
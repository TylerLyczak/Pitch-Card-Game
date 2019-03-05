import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player {
    Random randomGenerator;

    AIPlayer () {
        randomGenerator = new Random();
        hand = new Deck();
        trickDeck = new ArrayList<Card>();
        System.out.println("Bot Added");
    }


    public boolean playCard (ArrayList<Card> trick, int bot)    {
        /*
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        //System.out.println("RAnd time");
        //System.out.println(hand.cards.size());
        int ranIndex = randomGenerator.nextInt(hand.cards.size());
        //System.out.println(ranIndex);
        hand.cards.get(ranIndex).setPlayedBy(bot);
        trick.add(hand.cards.get(ranIndex));
        hand.cards.remove(ranIndex);
        return true;
    }

    public void makeBid (int highBid)  {
        //int ranIndex = randomGenerator.nextInt();
        ArrayList<Integer> nums = new ArrayList<Integer>();
        nums.add(1);
        for (int i=2; i<6; i++) {
            if (i > highBid)    {
                nums.add (i);
            }
        }
        int ranIndex = randomGenerator.nextInt(nums.size());
        bid = nums.get(ranIndex);
    }

    public void determineBid (int highBid)  {
        //int ranIndex = randomGenerator.nextInt();
        ArrayList<Integer> nums = new ArrayList<Integer>();
        nums.add(1);
        for (int i=2; i<6; i++) {
            if (i > highBid)    {
                nums.add (i);
            }
        }
        int ranIndex = randomGenerator.nextInt(nums.size());
        bid = nums.get(ranIndex);
    }
}

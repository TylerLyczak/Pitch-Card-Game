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

        int ranIndex = randomGenerator.nextInt(hand.cards.size());
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

    // Determines what bid the bot will make depending on their hand
    public void determineBid (ArrayList<Integer> previousBids) {
        // 0 = Diamonds
        // 1 = Clubs
        // 2 = Hearts
        // 3 = Spades
        ArrayList<Integer> suitWeights = new ArrayList<Integer>();
        int diamondWeight = 0;
        int clubWeight = 0;
        int heartWeight = 0;
        int spadeWeight = 0;
        for (int i=0; i<hand.getCards().size(); i++)    {
            if (hand.getCards().get(i).getSuit() == 'D')    {
                if (hand.getCards().get(i).getPoints() > 0) {
                    diamondWeight += hand.getCards().get(i).getPoints();
                }
            }
            else if (hand.getCards().get(i).getSuit() == 'C')   {
                if (hand.getCards().get(i).getPoints() > 0) {
                    clubWeight += hand.getCards().get(i).getPoints();
                }
            }
            else if (hand.getCards().get(i).getSuit() == 'H')   {
                if (hand.getCards().get(i).getPoints() > 0) {
                    heartWeight += hand.getCards().get(i).getPoints();
                }
            }
            else if (hand.getCards().get(i).getSuit() == 'S')   {
                if (hand.getCards().get(i).getPoints() > 0) {
                    spadeWeight += hand.getCards().get(i).getPoints();
                }
            }
        }

        suitWeights.add(diamondWeight);
        suitWeights.add(clubWeight);
        suitWeights.add(heartWeight);
        suitWeights.add(spadeWeight);

        // Gets the biggest weight of the four
        int highWeight = suitWeights.get(0);
        int highIndex = 0;
        for (int i=1; i<suitWeights.size(); i++)    {
            if (suitWeights.get(i) > highWeight)    {
                highWeight = suitWeights.get(i);
                highIndex = i;
            }
        }

        ArrayList<Integer> bidToChoose = new ArrayList<Integer>();
        bidToChoose.add(1);

        for (int i=0; i<previousBids.size(); i++)   {
            if (previousBids.get(i) == 5)   {
                bid = 1;
                return;
            }
        }

    }

}

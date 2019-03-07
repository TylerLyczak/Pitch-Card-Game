import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player {
    private Random randomGenerator;
    private double diamondWeight;
    private double spadeWeight;
    private double heartWeight;
    private double clubWeight;
    private double totalWeight;

    AIPlayer () {
        randomGenerator = new Random();
        hand = new Deck();
        trickDeck = new ArrayList<Card>();
        diamondWeight = 0;
        spadeWeight = 0;
        heartWeight = 0;
        clubWeight = 0;
    }

    public Random getRandomGenerator ()   { return randomGenerator;}

    public double getDiamondWeight ()   { return diamondWeight;}

    public double getSpadeWeight () { return spadeWeight;}

    public double getHeartWeight () { return heartWeight;}

    public double getClubWeight() { return clubWeight;}

    // Calculates the weights of each card by adding up the ranks and dividing by the total
    public void updateCardHandWeights ()    {
        if (hand.getCards().size() == 0)    {
            System.out.println("Hand empty");
            diamondWeight = 0;
            spadeWeight = 0;
            heartWeight = 0;
            clubWeight = 0;
            totalWeight = 0;
            return;
        }
        diamondWeight = 0;
        spadeWeight = 0;
        heartWeight = 0;
        clubWeight = 0;
        totalWeight = 0;
        // This for-loop goes through the bots hand and adds up the value of all the cards of each suit into separate weights
        for (int i=0; i<hand.getCards().size(); i++)    {
            if (hand.getCards().get(i).getSuit() == 'D')    {
                if (Card.cardRank(hand.getCards().get(i).getRank()) > 0) {
                    diamondWeight += Card.cardRank(hand.getCards().get(i).getRank());
                }
            }
            else if (hand.getCards().get(i).getSuit() == 'C')   {
                if (Card.cardRank(hand.getCards().get(i).getRank()) > 0) {
                    clubWeight += Card.cardRank(hand.getCards().get(i).getRank());
                }
            }
            else if (hand.getCards().get(i).getSuit() == 'H')   {
                if (Card.cardRank(hand.getCards().get(i).getRank()) > 0) {
                    heartWeight += Card.cardRank(hand.getCards().get(i).getRank());
                }
            }
            else if (hand.getCards().get(i).getSuit() == 'S')   {
                if (Card.cardRank(hand.getCards().get(i).getRank()) > 0) {
                    spadeWeight += Card.cardRank(hand.getCards().get(i).getRank());
                }
            }
        }
        // It then will
        totalWeight = diamondWeight + clubWeight + heartWeight + spadeWeight;
        diamondWeight = diamondWeight/totalWeight;
        clubWeight = clubWeight/totalWeight;
        heartWeight = heartWeight/totalWeight;
        spadeWeight = spadeWeight/totalWeight;
        System.out.println("Diamond Weight: " + diamondWeight);
        System.out.println("Club Weight: " + clubWeight);
        System.out.println("Heart Weight: " + heartWeight);
        System.out.println("Spade Weight: " + spadeWeight);
    }

    // Returns the char of the highest weight determined by the updateCardHandWeights()
    public char determineHighestWeight ()   {
        if (diamondWeight > spadeWeight && diamondWeight > heartWeight && diamondWeight > clubWeight)   {
            return 'D';
        }
        else if (spadeWeight > diamondWeight && spadeWeight > heartWeight && spadeWeight > clubWeight)  {
            return 'S';
        }
        else if (heartWeight > diamondWeight && heartWeight > spadeWeight && heartWeight > clubWeight)  {
            return 'H';
        }
        else    {
            return 'S';
        }
    }

    public boolean playCard (ArrayList<Card> trickList, ArrayList<Character> suits, int bot, char trump)   {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        updateCardHandWeights();

        boolean cardsPlayedRound;
        boolean cardsPlayedTurn;
        boolean trumpPlayedTurn;
        boolean trumpPlayedRound;
        ArrayList<Card> selectableCards = new ArrayList<Card>();


        // Case where its a new round and turn
        if (suits.size() == 0 && trickList.size() == 0) {
            cardsPlayedRound = false;
            cardsPlayedTurn = false;
            trumpPlayedRound = false;
            trumpPlayedTurn = false;
        }
        // Case where its a new turn but NOT round
        else if (suits.size() != 0 && trickList.size() == 0)    {
            cardsPlayedRound = true;
            cardsPlayedTurn = false;
            trumpPlayedRound = true;
            trumpPlayedTurn = false;
        }
        else    { // both suits and trickList != 0, suits cant == 0 when trickList !=0
            cardsPlayedRound = true;
            cardsPlayedTurn = true;
            trumpPlayedRound = true;
            trumpPlayedTurn = false;

            // Trump could have been played already by previous player
            for (int i=0; i<trickList.size(); i++)  {
                if (trickList.get(i).getSuit() == trump)    {
                    trumpPlayedTurn = true;
                }
            }
        }

        // Sets the Array list of cards the bot can play this turn
        if (suits.size() != 0)  {
            for (int i=0; i<hand.getCards().size(); i++)    {
                for (int j=0; j<suits.size(); j++)  {
                    if (hand.getCards().get(i).getSuit() == suits.get(j))   {
                        System.out.println("Selectable Cards are made");
                        selectableCards.add(hand.getCards().get(i));
                    }
                }
            }
        }
        // If there are no selectable cards, then the bot can play any card
        else    { selectableCards = hand.getCards();}

        /*
            Case 1 - Bot Goes First
            Bot will play a card of its highest suit and if it has atleast a queen
            Otherwise, it will play a suit of less value
            Bot can either:
                1. Play highest trump
                2. Play lowest >2 trump
                3. Play useless card
        */
        if (!cardsPlayedRound && !cardsPlayedTurn)  {
            System.out.println("Case 1");
            boolean atLeastQueen = false;
            boolean allTrumps = true;
            int highIndex = 0;
            Card highCard = null;
            int amountOfNonTrumps = 0;
            char highestSuit = determineHighestWeight();

            // Calculates if it has a card in its highest suit greater than a jack and if it has all trumps
            for (int i=0; i<hand.getCards().size(); i++)    {
                if (hand.getCards().get(i).getSuit() == highestSuit)    {
                    if (hand.getCards().get(i).getRankVal() > 11)  {
                        atLeastQueen = true;
                        highCard = hand.getCards().get(i);
                        highIndex = i;
                    }
                }
                else    { allTrumps = false; amountOfNonTrumps++;}
            }

            // Case 1.1 Plays that highest trump card
            if (atLeastQueen && amountOfNonTrumps <= 3) {
                // Finds the highest card of the highest suit
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i).getSuit() == highestSuit)    {
                        if (hand.getCards().get(i).getRankVal() > highCard.getRankVal())  {
                            highCard = hand.getCards().get(i);
                            highIndex = i;
                        }
                    }
                }
                // Plays the card
                System.out.println("Case 1.1 High Trump");
                hand.getCards().get(highIndex).setPlayedBy(bot);
                trickList.add(hand.getCards().get(highIndex));
                hand.getCards().remove(highIndex);
                return true;
            }
            // Case 1.2 If it doesn't have a high trump but only has cards of the same suit,
            // then it will play the lowest value it has besides two since that can be a point
            else if (allTrumps) {
                int lowTrumpIndex = -1;
                Card lowTrump = null;

                // This loop finds any low card > 2 and returns it
                for (int i=0; i<hand.getCards().size(); i++)    {
                    //
                    if (hand.getCards().get(i).getRankVal() != 2 && hand.getCards().get(i) != highCard)  {
                        lowTrumpIndex = i;
                        lowTrump = hand.getCards().get(i);
                        break;
                    }
                }
                // This loop will make sure its the lowest besides 2
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i).getRankVal() != 2)    {
                        if (hand.getCards().get(i).getRankVal() < lowTrump.getRankVal())    {
                            lowTrump = hand.getCards().get(i);
                            lowTrumpIndex = i;
                        }
                    }
                }
                // Plays the card
                System.out.println("Case 1.2 >2 Trump");
                hand.getCards().get(lowTrumpIndex).setPlayedBy(bot);
                trickList.add(hand.getCards().get(lowTrumpIndex));
                hand.getCards().remove(lowTrumpIndex);
                return true;
            }
            // Case 1.3 If it cant find any high value trump card, it will return one of its low cards
            else    {
                // Adds any non trump card to an array list
                ArrayList<Card> nonTrumps = new ArrayList<Card>();
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i).getSuit() != trump)  {
                        nonTrumps.add(hand.getCards().get(i));
                    }
                }

                // Loops through the array list to find the lowest card
                Card lowNonTrump = nonTrumps.get(0);
                for (int i=0; i<nonTrumps.size(); i++)  {
                    if (Card.cardRank(nonTrumps.get(i).getRank()) < Card.cardRank(lowNonTrump.getRank()))   {
                        if (Card.cardRank(nonTrumps.get(i).getRank()) > 2)  {
                            lowNonTrump = nonTrumps.get(i);
                        }
                    }
                }

                // Loops through the hand to find the actual index
                int realIndex = 0;
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (lowNonTrump == hand.getCards().get(i))  {
                        realIndex = i;
                        break;
                    }
                }
                // Plays the card
                System.out.println("Case 1.3 >2 nonTrump");
                hand.getCards().get(realIndex).setPlayedBy(bot);
                trickList.add(hand.getCards().get(realIndex));
                hand.getCards().remove(realIndex);
                return true;
            }
        }
        /*
            Case 2 - Bot plays card first turn but isn't first round
            So bot won a trick
            Bot can either:
                1. Play highest trump
                2. Play lowest >2 trump
                3. Play useless card
        */
        else if (cardsPlayedRound && !cardsPlayedTurn)  {
            System.out.println("Case 2");
            boolean atLeastQueen = false;
            boolean allTrumps = true;
            int highIndex = 0;
            Card highCard = null;
            int amountOfNonTrumps = 0;
            char highestSuit = determineHighestWeight();

            // Calculates if it has a card in its highest suit greater than a jack and if it has all trumps
            for (int i=0; i<selectableCards.size(); i++)    {
                if (selectableCards.get(i).getSuit() == highestSuit)    {
                    if (Card.cardValue(selectableCards.get(i).getRank()) > 11)  {
                        atLeastQueen = true;
                        highCard = selectableCards.get(i);
                    }
                }
                else    { allTrumps = false; amountOfNonTrumps++;}
            }

            // Case 2.1 Plays that highest trump card
            if (atLeastQueen && amountOfNonTrumps <= 3) {
                // Finds the highest card of the highest suit
                for (int i=0; i<selectableCards.size(); i++)    {
                    if (selectableCards.get(i).getSuit() == highestSuit)    {
                        if (Card.cardValue(selectableCards.get(i).getRank()) > Card.cardRank(highCard.getRank()))  {
                            highCard = selectableCards.get(i);
                            highIndex = i;
                        }
                    }
                }

                // Finds the index of that highCard
                int realIndex = 0;
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i) == highCard) {
                        realIndex = i;
                        break;
                    }
                }
                // Plays the card
                System.out.println("Case 2.1 Plays that highest trump card");
                hand.getCards().get(realIndex).setPlayedBy(bot);
                trickList.add(hand.getCards().get(realIndex));
                hand.getCards().remove(realIndex);
                return true;
            }
            // Case 2.2
            // If it doesn't have a high trump but only has cards of the same suit, then it will play the lowest value
            // it has besides two since that can be a point
            else if (allTrumps) {
                Card lowTrump = null;

                // This loop finds any low card > 2 and returns it
                for (int i=0; i<selectableCards.size(); i++)    {
                    //
                    if (Card.cardRank(selectableCards.get(i).getRank()) != 2 && selectableCards.get(i) != highCard)  {
                        lowTrump = selectableCards.get(i);
                        break;
                    }
                }

                // Finds the index of that highCard
                int realIndex = 0;
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i) == lowTrump) {
                        realIndex = i;
                        break;
                    }
                }
                // Plays the card
                System.out.println("Case 2.2 Plays trump card that is >2");
                hand.getCards().get(realIndex).setPlayedBy(bot);
                trickList.add(hand.getCards().get(realIndex));
                hand.getCards().remove(realIndex);
                return true;
            }
            // Case 2.3
            // If it cant find any high value trump card, it will return one of its low cards
            else    {
                // Adds any non trump card to an array list
                ArrayList<Card> nonTrumps = new ArrayList<Card>();
                for (int i=0; i<selectableCards.size(); i++)    {
                    if (selectableCards.get(i).getSuit() != trump)  {
                        nonTrumps.add(selectableCards.get(i));
                    }
                }
                if (nonTrumps.size() != 0) {
                    // Loops through the array list to find the lowest card
                    Card lowNonTrump = nonTrumps.get(0);
                    for (int i = 0; i < nonTrumps.size(); i++) {
                        if (Card.cardRank(nonTrumps.get(i).getRank()) < Card.cardRank(lowNonTrump.getRank())) {
                            if (Card.cardRank(nonTrumps.get(i).getRank()) > 2) {
                                lowNonTrump = nonTrumps.get(i);
                            }
                        }
                    }

                    // Loops through the hand to find the actual index
                    int realIndex = 0;
                    for (int i = 0; i < hand.getCards().size(); i++) {
                        if (lowNonTrump == hand.getCards().get(i)) {
                            realIndex = i;
                            break;
                        }
                    }
                    // Plays the card
                    System.out.println("Case 2.3 >2 nonTrump");
                    hand.getCards().get(realIndex).setPlayedBy(bot);
                    trickList.add(hand.getCards().get(realIndex));
                    hand.getCards().remove(realIndex);
                    return true;
                }
                else    {
                    Card lowNonTrump = selectableCards.get(0);
                    // Loops through the array list to find the lowest card
                    for (int i = 0; i < nonTrumps.size(); i++) {
                        if (Card.cardRank(nonTrumps.get(i).getRank()) < Card.cardRank(lowNonTrump.getRank())) {
                            if (Card.cardRank(nonTrumps.get(i).getRank()) > 2) {
                                lowNonTrump = nonTrumps.get(i);
                            }
                        }
                    }

                    // Loops through the hand to find the actual index
                    int realIndex = 0;
                    for (int i = 0; i < hand.getCards().size(); i++) {
                        if (lowNonTrump == hand.getCards().get(i)) {
                            realIndex = i;
                            break;
                        }
                    }
                    // Plays the card
                    System.out.println("Case 2.3 >2 nonTrump");
                    hand.getCards().get(realIndex).setPlayedBy(bot);
                    trickList.add(hand.getCards().get(realIndex));
                    hand.getCards().remove(realIndex);
                    return true;
                }
            }
        }
        /*
            Case 3 - Bot plays during the middle of a round and turn
            So some tricks have been won but cards remain in the hand
            Bot can either:
                1. Beat trump played
                2. Can't beat trump played but has to play trump card
                3. Can't beat trump but doesn't have card to beat trump
                4. No trump was played, so bot plays trump
                5. No trump was played, but bot has a higher card
                6. No trump was played, but bot plays a useless card
        */
        else if (cardsPlayedRound && cardsPlayedTurn)  {
            System.out.println("Case 3");
            boolean trumpPlayed = false;
            boolean hasTrump = false;
            boolean beatTrumpPlayed = false;
            boolean onlyTrump = true;
            Card highTrumpPlayed = null;
            Card highestCardPlayed = null;
            Card cardToBeatTrump = null;
            Card cardToLoseTrump = null;
            Card lowestCardInHand = null;
            Card highestCardInHand = null;

            // Sees if the bots hand has trump in it also gets there lowest and highest card non trump cards
            for (int i=0; i<selectableCards.size(); i++)    {
                if (selectableCards.get(i).getSuit() == trump)  {
                    hasTrump = true;
                    cardToBeatTrump = selectableCards.get(i);
                    cardToLoseTrump = selectableCards.get(i);
                }
                else    {
                    onlyTrump = false;
                    lowestCardInHand = selectableCards.get(i);
                    highestCardInHand = selectableCards.get(i);
                }
            }
            // If trump card was found, this will search for it in the hand
            if (hasTrump)   {
                for (int i=0; i<selectableCards.size(); i++)    {
                    if (selectableCards.get(i).getSuit() == trump)  {
                        if (Card.cardRank(selectableCards.get(i).getRank()) > Card.cardRank(cardToBeatTrump.getRank())) {
                            cardToBeatTrump = selectableCards.get(i);
                        }
                        if (Card.cardRank(selectableCards.get(i).getRank()) > Card.cardRank(cardToLoseTrump.getRank())) {
                            cardToLoseTrump = selectableCards.get(i);
                        }
                    }
                }
            }
            // Gets the lowest non trump cards
            else    {
                for (int i=0; i<selectableCards.size(); i++)    {
                    if (Card.cardRank(selectableCards.get(i).getRank()) > Card.cardRank(highestCardInHand.getRank()))   {
                        highestCardInHand = selectableCards.get(i);
                    }
                    if (Card.cardRank(selectableCards.get(i).getRank()) < Card.cardRank(lowestCardInHand.getRank()))    {
                        lowestCardInHand = selectableCards.get(i);
                    }
                }
            }

            // Checks if a trump was played
            for (int i=0; i<trickList.size(); i++)  {
                if (trickList.get(i).getSuit() == trump)    {
                    trumpPlayed = true;
                    highTrumpPlayed = trickList.get(i);
                }
                else    { highestCardPlayed = trickList.get(i);}
            }

            // If trump is played, it will get that card
            if (trumpPlayed)    {
                for (int i=0; i<trickList.size(); i++)  {
                    if (trickList.get(i).getSuit() == trump)    {
                        if (Card.cardRank(trickList.get(i).getRank()) > Card.cardRank(highTrumpPlayed.getRank()))   {
                            highTrumpPlayed = trickList.get(i);
                        }
                    }
                }

            }
            // Gets the highest card out of the trickList
            else    {
                for (int i=0; i<trickList.size(); i++)  {
                    if (Card.cardRank(trickList.get(i).getRank()) > Card.cardRank(highestCardPlayed.getRank())) {
                        highestCardPlayed = trickList.get(i);
                    }
                }
            }

            if (trumpPlayed && hasTrump)    {
                if (Card.cardRank(cardToBeatTrump.getRank()) > Card.cardRank(highTrumpPlayed.getRank()))    {
                    beatTrumpPlayed = true;
                }   else    { beatTrumpPlayed = false;}
            }

            // Case 3.1 Beat Trump Card
            if (beatTrumpPlayed && trumpPlayed && hasTrump && onlyTrump) {

                // For-loop to find the real index of cardToBeatTrump
                int realIndex = 0;
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i) == cardToBeatTrump)  {
                        realIndex = i;
                    }
                }
                // Plays the card
                System.out.println("Case 3.1 Beat trump played");
                hand.getCards().get(realIndex).setPlayedBy(bot);
                trickList.add(hand.getCards().get(realIndex));
                hand.getCards().remove(realIndex);
                return true;
            }
            // Case 3.2 Can't beat trump but has to play trump since bot only has trumps
            else if (!beatTrumpPlayed && trumpPlayed && hasTrump && onlyTrump)   {
                // For-loop to find the real index of cardToBeatTrump
                int realIndex = 0;
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i) == cardToLoseTrump)  {
                        realIndex = i;
                    }
                }
                // Plays the card
                System.out.println("Case 3.2 Can't beat trump but has to play trump since bot only has trumps");
                hand.getCards().get(realIndex).setPlayedBy(bot);
                trickList.add(hand.getCards().get(realIndex));
                hand.getCards().remove(realIndex);
                return true;
            }
            // Case 3.3 Can't beat trump but doesn't have card to beat trump
            else if (!beatTrumpPlayed && trumpPlayed && !hasTrump && !onlyTrump)    {
                // For-loop to find the real index of cardToBeatTrump
                int realIndex = 0;
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i) == lowestCardInHand)  {
                        realIndex = i;
                    }
                }
                // Plays the card
                System.out.println("Case 3.3 Can't beat trump but doesn't have card to beat trump");
                hand.getCards().get(realIndex).setPlayedBy(bot);
                trickList.add(hand.getCards().get(realIndex));
                hand.getCards().remove(realIndex);
                return true;
            }
            // Case 3.4 No trump was played, so bot plays trump
            else if (!beatTrumpPlayed && !trumpPlayed && hasTrump)  {
                // For-loop to find the real index of cardToBeatTrump
                int realIndex = 0;
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i) == cardToBeatTrump)  {
                        realIndex = i;
                    }
                }
                // Plays the card
                System.out.println("Case 3.4 No trump was played, so bot plays trump");
                hand.getCards().get(realIndex).setPlayedBy(bot);
                trickList.add(hand.getCards().get(realIndex));
                hand.getCards().remove(realIndex);
                return true;
            }
            // Case 3.5 No trump was played, but bot has a higher card
            else if  (!beatTrumpPlayed && !trumpPlayed && !hasTrump && !onlyTrump)  {
                // For-loop to find the real index of cardToBeatTrump
                int realIndex = 0;
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i) == highestCardInHand)  {
                        realIndex = i;
                    }
                }
                // Plays the card
                System.out.println("Case 3.5 No trump was played, but bot has a higher card");
                hand.getCards().get(realIndex).setPlayedBy(bot);
                trickList.add(hand.getCards().get(realIndex));
                hand.getCards().remove(realIndex);
                return true;
            }
            // Case 3.6 No trump was played, but bot plays a useless card
            else    {
                // For-loop to find the real index of cardToBeatTrump
                int realIndex = 0;
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i) == lowestCardInHand)  {
                        realIndex = i;
                    }
                }
                // Plays the card
                System.out.println("Case 3.6 No trump was played, but bot plays a useless card");
                hand.getCards().get(realIndex).setPlayedBy(bot);
                trickList.add(hand.getCards().get(realIndex));
                hand.getCards().remove(realIndex);
                return true;
            }
        }
        /*
            Case 4
            Uses a random number generator to make sure a bot will always play a card
            This case is never used but having a fail-safe allows the program to keep
            running no matter what
        */
        else    {
            System.out.println("Case 4 OOOOOOOOOOOOOOOOOOOO SHHHHHHHHHHIIIIIIIITTTTTTTTTT    bot: " + bot);
            int ranIndex = randomGenerator.nextInt(selectableCards.size());
            Card choosenCard = selectableCards.get(ranIndex);
            int realIndex = 0;
            for (int i=0; i<hand.getCards().size(); i++)    {
                if (hand.getCards().get(i) == choosenCard)  {
                    realIndex = i;
                }
            }
            hand.getCards().get(realIndex).setPlayedBy(bot);
            trickList.add(hand.getCards().get(realIndex));
            hand.getCards().remove(ranIndex);
            return true;
        }
    }

    public boolean playCard2 (ArrayList<Card> trickList, ArrayList<Character> suits, int bot, char trump)   {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateCardHandWeights();
        System.out.println("Hand: ");
        for (int i=0; i<hand.getCards().size(); i++)    {
            System.out.print(" " + hand.getCards().get(i).getSuit() + hand.getCards().get(i).getRank());
        }
        System.out.println("\n");

        // Makes a new list of cards with only cards that can be played at the time
        ArrayList<Card> selectableCards = new ArrayList<Card>();
        //ArrayList<Card> nonSelectableCards = new ArrayList<Card>();
        boolean cardsPlayedRound = false;
        if (suits.size() != 0)  {
            cardsPlayedRound = true;
            // change playable cards
            for (int i=0; i<hand.getCards().size(); i++)    {
                //selectableCards.add(hand.getCards().get(i));
                for (int j=0; j<suits.size(); j++)  {
                    if (hand.getCards().get(i).getSuit() == suits.get(j))   {
                        System.out.println("Selectable Cards are made");
                        selectableCards.add(hand.getCards().get(i));
                    }
                }
            }
            // Removes all the cards the AI cant play so it follows the rules
            //selectableCards.removeAll(nonSelectableCards);
        }

        if (selectableCards.size() == 0)    {
            selectableCards = hand.getCards();
        }

        // Determines how the AI will choose a card
        boolean cardsPlayedTurn = false;
        if (trickList.size() == 0)  { cardsPlayedTurn = false;}
        else    { cardsPlayedTurn = true;}

        // Case 1
        // Plays the highest suit card it has
        // Case where the bot is the first to go in the round
        if (!cardsPlayedRound && !cardsPlayedTurn)   {
            System.out.println("Case 1");
            // Gets the first card of the highest suit in the hand
            Card highCard = null;
            int index = 0;
            for (int i=0; i<hand.getCards().size(); i++)    {
                if (hand.getCards().get(i).getSuit() == determineHighestWeight())   {
                    highCard = hand.getCards().get(i);
                    index = i;
                    break;
                }
            }
            // This loop will get the highest card in the suit the bot has most of
            for (int i=0; i<hand.getCards().size(); i++)    {
                if (Card.cardRank(highCard.getRank()) < Card.cardRank(hand.getCards().get(i).getRank()))    {
                    if (hand.getCards().get(i).getSuit() == determineHighestWeight())   {
                        highCard = hand.getCards().get(i);
                        index = i;
                    }
                }
            }
            System.out.println("Case 1 Success");
            // Plays the card
            hand.getCards().get(index).setPlayedBy(bot);
            trickList.add(hand.getCards().get(index));
            hand.getCards().remove(index);
            return true;
        }
        // Case 2
        // Case where cards were played by a previous trick, bot won trick
        else if (cardsPlayedRound && !cardsPlayedTurn)  {
            System.out.println("Case 2");
            Card highCard = null;
            int index = 0;
            // Gets the first card of the highest suit in the hand
            for (int i=0; i<selectableCards.size(); i++)    {
                if (selectableCards.get(i).getSuit() == determineHighestWeight())   {
                    highCard = selectableCards.get(i);
                    index = i;
                    break;
                }
            }
            for (int i=0; i<selectableCards.size(); i++)    {
                if (Card.cardRank(highCard.getRank()) < Card.cardRank(selectableCards.get(i).getRank()))    {
                    highCard = selectableCards.get(i);
                    index = i;
                }
            }
            int handIndex = -1;
            for (int i=0; i<hand.getCards().size(); i++)    {
                if (hand.getCards().get(i) == highCard) {
                    handIndex = i;
                    break;
                }
            }
            System.out.println("Case 2 Success");
            // Plays the card
            hand.getCards().get(handIndex).setPlayedBy(bot);
            trickList.add(hand.getCards().get(handIndex));
            hand.getCards().remove(handIndex);
            return true;
        }

        // else case where the bot is not the first to go
        // Sees if there is a trump in the tricklist
        boolean trumpPlayed = false;
        int trumpRank = 0;
        for (int i=0; i<trickList.size(); i++)  {
            if (trickList.get(i).getSuit() == trump)    {
                trumpPlayed = true;
                if (Card.cardRank(trickList.get(i).getRank()) > trumpRank)  {
                    trumpRank = Card.cardRank(trickList.get(i).getRank());
                }
            }
        }
        // Sees if the bot has a higher trump card to beat the trumps in the tricklist
        boolean hasTrump = false;
        boolean onlyTrumps = true;
        int hasTrumpRank = 0;
        Card hasTrumpCard = null;
        for (int i=0; i<selectableCards.size(); i++)    {
            if (selectableCards.get(i).getSuit() == trump)  {
                hasTrump = true;
                for (int j=0; j<trickList.size(); j++)  {
                    if (Card.cardRank(trickList.get(j).getRank()) > hasTrumpRank)  {
                        hasTrumpRank = Card.cardRank(selectableCards.get(i).getRank());
                        hasTrumpCard = selectableCards.get(i);
                    }
                }
            }
            else    { onlyTrumps = false; break;}
        }

        // Sees if the bots trump is higher than the tricklist trump high, if not, it will play a less valuable card
        if (trumpPlayed && hasTrump)    {
            System.out.println("Case 3");
            // If the bots trump is higher, than it will play its highest
            if (hasTrumpRank > trumpRank)   {
                int handIndexTrump = -1;
                for (int i=0; i<hand.getCards().size(); i++)    {
                    if (hand.getCards().get(i) == hasTrumpCard) {
                        handIndexTrump = i;
                        break;
                    }
                }
                System.out.println("Case 3 Success");
                // Playes the card
                hand.getCards().get(handIndexTrump).setPlayedBy(bot);
                trickList.add(hand.getCards().get(handIndexTrump));
                hand.getCards().remove(handIndexTrump);
                return true;
            }
        }

        // Bot wont play its high trump if it doesn't have too, it will play any low non trump card it has
        if (hasTrump && !onlyTrumps)   {
            System.out.println("Case 4");
            Card lowNonTrump = null;
            int lowNonTrumpIndex = -1;
            for (int i=0; i<selectableCards.size(); i++)    {
                if (selectableCards.get(i).getSuit() != trump)  {
                    lowNonTrump = selectableCards.get(i);
                    lowNonTrumpIndex = i;
                    break;
                }
            }
            // Now that we found the non trump card, we need to make sure its the lowest
            for (int i=0; i<selectableCards.size(); i++)    {
                if (selectableCards.get(i).getSuit() != trump)   {
                    if (Card.cardRank(lowNonTrump.getRank()) > Card.cardRank(selectableCards.get(i).getRank())) {
                        lowNonTrump = selectableCards.get(i);
                        lowNonTrumpIndex = i;
                    }
                }
            }
            // Gets the index of the low non trump card in the hand
            int handLow = -1;
            for (int i=0; i<hand.getCards().size(); i++)    {
                if (hand.getCards().get(i) == lowNonTrump)  {
                    handLow = i;
                    break;
                }
            }
            System.out.println("Case 4 Success");
            // Playes the card
            hand.getCards().get(handLow).setPlayedBy(bot);
            trickList.add(hand.getCards().get(handLow));
            hand.getCards().remove(handLow);
            return true;
        }

        // Case where the bot only has trumps but can't beat the high trump in the tricklist, so it will try to play
        // any of its middle trump cards

        if (onlyTrumps) {
            System.out.println("Case 5");
            Card lowTrump = selectableCards.get(0);
            Card highTrump = selectableCards.get(0);
            Card middleTrump = null;
            int lowTrumpIndex = 0;
            int highTrumpIndex = 0;
            int middleTrumpIndex = -1;
            for (int i=0; i<selectableCards.size(); i++)    {
                if (Card.cardRank(lowTrump.getRank()) > Card.cardRank(selectableCards.get(i).getRank()))    {
                    lowTrump = selectableCards.get(i);
                    lowTrumpIndex = i;
                }
                if (Card.cardRank(highTrump.getRank()) < Card.cardRank(selectableCards.get(i).getRank()))   {
                    highTrump = selectableCards.get(i);
                    highTrumpIndex = i;
                }
            }
            middleTrumpIndex = highTrumpIndex - lowTrumpIndex;
            System.out.println("middleTrump: " + middleTrumpIndex);
            middleTrump = selectableCards.get(middleTrumpIndex);
            // Gets the index of the middle trump in the hand
            int newMiddleIndex = -1;
            for (int i=0; i<hand.getCards().size(); i++)    {
                if (hand.getCards().get(i) == middleTrump)  {
                    newMiddleIndex = i;
                    break;
                }
            }
            System.out.println("Case 5 Success");
            // Playes the card
            hand.getCards().get(newMiddleIndex).setPlayedBy(bot);
            trickList.add(hand.getCards().get(newMiddleIndex));
            hand.getCards().remove(newMiddleIndex);
            return true;
        }

        System.out.println("Case 6 Success");
        // Case where all else fails, it will play a random card
        // Never happens but it is safe to have included
        System.out.println("OOOOOOOOOOOOOO SHHIIIIIITTTTTTTTTTT  bot: " + bot);
        int ranIndex = randomGenerator.nextInt(hand.cards.size());
        hand.cards.get(ranIndex).setPlayedBy(bot);
        trickList.add(hand.cards.get(ranIndex));
        hand.cards.remove(ranIndex);
        return true;

    }

    // Determines what bid the bot will make depending on their hand
    public void determineBid (ArrayList<Integer> previousBids) {
        updateCardHandWeights ();

        // If any of the weights are 1, then they have a really good change of winning the game, making
        // them bid smudge.
        if (diamondWeight > 1 || clubWeight > 1 || heartWeight > 1 || spadeWeight > .1)  {
            bid = 5;
            return;
        }

        // Makes a percentage Array List to see the probability of each suit
        ArrayList<Double> weightPercentage = new ArrayList<Double>();
        weightPercentage.add( diamondWeight/ totalWeight);
        weightPercentage.add ( clubWeight/ totalWeight);
        weightPercentage.add ( heartWeight/ totalWeight);
        weightPercentage.add ( spadeWeight/ totalWeight);



        ArrayList<Integer> bidToChoose = new ArrayList<Integer>();
        bidToChoose.add(1);
        // Gets the highest bid to determine what the bot can choose from
        // If the previous bid is a 5 or smudge, then its bid will automatically be 0
        int highBid = 0;
        for (int i=0; i<previousBids.size(); i++)   {
            if (previousBids.get(i) == 5)   {
                bid = 1;
                return;
            }
            if (highBid < previousBids.get(i))  {
                highBid = previousBids.get(i);
            }
        }

        // Since one is already added, it starts at 2
        for (int i=2; i<6; i++)    {
            if (highBid < i)    {
                bidToChoose.add(i);
            }
        }

        // This for loop loops through each percentage and determines what bid it should do based on its percentage.
        ArrayList<Integer> calcBid = new ArrayList<Integer>();
        for (int i=0; i<weightPercentage.size(); i++)   {
            System.out.println("Percentage: " + weightPercentage.get(i));
            if (weightPercentage.get(i) > 0.5 && bidToChoose.contains(2))   {
                calcBid.add(2);
            }
            else if (weightPercentage.get(i) >= 0.5 && bidToChoose.contains(3))  {
                calcBid.add(3);
            }
            else if (weightPercentage.get(i) >= 0.6 && bidToChoose.contains(3))  {
                calcBid.add(3);
            }
            else if (weightPercentage.get(i) >= 0.65 && bidToChoose.contains(4)) {
                calcBid.add(4);
            }
            else if (weightPercentage.get(i) >= 0 && bidToChoose.contains(1))    {
                calcBid.add(1);
            }
            else if (weightPercentage.get(i) >= 0.1 && bidToChoose.contains(1))   {
                calcBid.add(1);
            }
            else if (weightPercentage.get(i) >= 0.2 && bidToChoose.contains(1))   {
                calcBid.add(1);
            }
            else if (weightPercentage.get(i) >= 0.35 && bidToChoose.contains(2))  {
                calcBid.add(2);
            }
            else if (weightPercentage.get(i) >= 0.4 && bidToChoose.contains(2))  {
                calcBid.add(2);
            }
            else if (weightPercentage.get(i) >= 0.45 && bidToChoose.contains(3)) {
                calcBid.add(3);
            }
        }

        // For-loop that gets the highest bid placed into the array list
        int finalBid = 0;
        for (int i=0; i<calcBid.size(); i++)    {
            if (calcBid.get(i) > finalBid)  {
                finalBid = calcBid.get(i);
                System.out.println("Final bid: " + finalBid);
            }
        }

        // Weights were not high enough to make a bid
        if (finalBid == 0)  { bid = 1; previousBids.add(1);}
        // Returns the highest bid.
        else    { bid = finalBid; previousBids.add(finalBid);}
    }

}

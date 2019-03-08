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
        ArrayList<Card> selectableCards = new ArrayList<Card>();


        // Case where its a new round and turn
        if (suits.size() == 0 && trickList.size() == 0) {
            cardsPlayedRound = false;
            cardsPlayedTurn = false;
        }
        // Case where its a new turn but NOT round
        else if (suits.size() != 0 && trickList.size() == 0)    {
            cardsPlayedRound = true;
            cardsPlayedTurn = false;
        }
        else    { // both suits and trickList != 0, suits cant == 0 when trickList !=0
            cardsPlayedRound = true;
            cardsPlayedTurn = true;
        }

        // Sets the Array list of cards the bot can play this turn
        if (suits.size() != 0)  {
            for (int i=0; i<hand.getCards().size(); i++)    {
                for (int j=0; j<suits.size(); j++)  {
                    if (hand.getCards().get(i).getSuit() == suits.get(j))   {
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

    // Determines what bid the bot will make depending on their hand
    public void determineBid (ArrayList<Integer> previousBids) {
        updateCardHandWeights ();

        // If any of the weights are 1, then they have a really good change of winning the game, making
        // them bid smudge.
        if (diamondWeight > 1.0 || clubWeight > 1.0 || heartWeight > 1.0 || spadeWeight > 1.0)  {
            bid = 5;
            return;
        }

        // Makes a percentage Array List to see the probability of each suit
        ArrayList<Double> weightPercentage = new ArrayList<Double>();
        weightPercentage.add(diamondWeight);
        weightPercentage.add(clubWeight);
        weightPercentage.add(heartWeight);
        weightPercentage.add(spadeWeight);



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
            }
        }

        // Weights were not high enough to make a bid
        if (finalBid == 0)  { bid = 1; previousBids.add(1);}
        // Returns the highest bid.
        else    { bid = finalBid; previousBids.add(finalBid);}
    }

}

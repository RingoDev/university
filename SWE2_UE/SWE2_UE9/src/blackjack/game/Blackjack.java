package blackjack.game;

import blackjack.player.Dealer;
import blackjack.player.HumanPlayer;
import blackjack.player.Player;
import inout.In;
import inout.Out;

import java.util.*;

public class Blackjack {


    public static final int TOTAL_NUM_CARDS = 52;
    private final List<Integer> deck;
    private final HumanPlayer human;
    private final Dealer dealer;

    public Blackjack() {
        deck = new ArrayList<>();
        human = new HumanPlayer();
        dealer = new Dealer();
    }

    public List<Integer> getDeck() {
        return deck;
    }

    public void shuffle() {
        if (deck.size() == 0) for (int i = 0; i < TOTAL_NUM_CARDS; i++) deck.add(i);
    }

    public HumanPlayer getHumanPlayer() {
        return human;
    }

    public Dealer getDealer() {
        return dealer;
    }

    /**
     * evaluate which player won this round
     *
     * @return the result of this round
     */
    public GameResult evaluateCards() {
        if (human.isBusted()) return GameResult.DealerWins;
        if (human.hasBlackJack() && dealer.hasBlackJack()) return GameResult.Draw;
        if (dealer.isBusted()) return GameResult.PlayerWins;
        if (human.hasBlackJack()) return GameResult.PlayerWins;
        if (dealer.hasBlackJack()) return GameResult.DealerWins;
        if (human.getValue() > dealer.getValue()) return GameResult.PlayerWins;
        if (dealer.getValue() > human.getValue()) return GameResult.DealerWins;
        return GameResult.Draw;
    }

    /**
     * draw a random card that is still in the deck
     *
     * @return the selected card
     * @throws OutOfCardsException when no more cards are available
     */
    public Card drawCard() throws OutOfCardsException {
        if (deck.size() == 0) throw new OutOfCardsException();//would be nicer to just shuffle the cards but the Error is a requirement
        Random r = new Random();
        int drawIndex = r.nextInt(deck.size());
        deck.remove(drawIndex);
        return new Card(drawIndex);
    }


    void humanTurns() {
        while (true) {
            switch (human.makeTurn()) {
                case Stay:
                    return;
                case Hit:
                    human.addCard(drawCard());
                    printGameState();
                    break;
                case DoubleDown:
                    if(human.getChips() < human.getWager()){
                        System.out.println("You dont have enough money to double down!\n");
                        break;
                    }else{
                        human.addCard(drawCard());
                        human.setChips(human.getChips() - human.getWager());
                        human.setWager(human.getWager()*2);
                        printGameState();
                        return;
                    }
            }
            if (human.isBusted() || human.hasBlackJack()) {
                return;
            }
        }
    }

    void dealerTurns() {
        while (true) {
            if (human.hasBlackJack()) {
                if (dealer.getValue() > 9) dealer.addCard(drawCard());
                else break;//cant beat human anymore
            } else {
                if (dealer.makeTurn() == Turn.Stay) {
                    break;
                } else {
                    dealer.addCard(drawCard());
                    printGameState();
                }
            }
        }

    }

    public void setWager() {
        int wager = 1; // can be modified to take different wagers as input
        human.setChips(human.getChips() - wager);
        human.setWager(wager);
    }

    public void setStartingChips() {
        int chipStack = 10;// can be modified to take different startingStacks as input
        human.setChips(chipStack);
    }

    /**
     * distributes the winnings according to game result.
     */
    public void distributeWinnings() {

        int winnings;

        if (evaluateCards() == GameResult.PlayerWins) {
            if (human.hasBlackJack()) winnings = human.getWager() * 3;
            else winnings = human.getWager() * 2;
        }
        else if(evaluateCards() == GameResult.DealerWins) {
            winnings = 0;
        }
        else winnings = human.getWager();
        human.setChips(human.getChips() + winnings);
    }

    /**
     * play the game
     */
    public void play() {

        setStartingChips();

        System.out.println("You chose to sit down with " + human.getChips() + " chips.\nGood Luck!\n");

        shuffle();

        while (true) {

            setWager();

            dealer.addCard(drawCard());
            human.addCard(drawCard());
            human.addCard(drawCard());
            printGameState();

            if(!human.hasBlackJack())humanTurns();

            if (!human.isBusted()) dealerTurns();

            switch (evaluateCards()) {
                case PlayerWins:
                    System.out.println("Player wins" + (human.hasBlackJack()? " with BlackJack" : "")+ "!");
                    break;
                case DealerWins:
                    System.out.println("Dealer wins" + (dealer.hasBlackJack()? " with BlackJack" : "")+ "!");
                    break;
                case Draw:
                    System.out.println("Draw!");
                    break;
            }

            distributeWinnings();

            if (human.getChips() < 1) {
                System.out.println("You dont have any chips left to continue playing");
                break;
            }

            System.out.println("You have " + human.getChips() + " chips. If you want to continue playing enter: y\n");
            if (In.readChar() != 'y') {
                System.out.println("\nCome back any time soon!");
                break;
            } else {
                System.out.println("=============================================================");
                System.out.println();
                human.reset();
                dealer.reset();
            }
        }
    }

    /**
     * print the cards of both players and their value
     */
    public void printGameState() {

        Out.println("Dealer (" + dealer.getValue() + ")");
        for (Card c : dealer.getCards()) {
            Out.print(c + " ");
        }
        Out.println();

        Out.println("Player (" + human.getValue() + ")");
        for (Card c : human.getCards()) {
            Out.print(c + " ");
        }
        Out.println("\n");
    }
}

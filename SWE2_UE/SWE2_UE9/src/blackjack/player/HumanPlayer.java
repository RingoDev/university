package blackjack.player;

import blackjack.game.Turn;
import inout.*;

public class HumanPlayer extends Player {

    int chips;

    public HumanPlayer() {
        super();
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    @Override
    public Turn makeTurn() {
        while (true) {
            System.out.println("What do you want to do?");
            System.out.println("Stay (s)");
            System.out.println("Hit (h)");
            if (cards.size() == 2) System.out.println("Double down (d)");
            switch (In.readChar()) {
                case 's':
                    System.out.println("Player decided to stay.\n");
                    return Turn.Stay;
                case 'h':
                    System.out.println("Player decided to hit another card.\n");
                    return Turn.Hit;
                case 'd':
                    if (cards.size() == 2) {
                        System.out.println("Player decided to double the wager.\n");
                        return Turn.DoubleDown;
                    }
                    System.out.println("Can't DoubleDown! Try again.\n");
                    break;
                default:
                    System.out.println("Wrong input, please try again.\n");
                    break;
            }
        }
    }
}

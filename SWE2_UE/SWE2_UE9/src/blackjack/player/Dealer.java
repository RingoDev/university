package blackjack.player;

import blackjack.game.Turn;

public class Dealer extends Player {

    public Dealer() {
        super();
    }

    @Override
    public Turn makeTurn() {
        return getValue() > 16 ? Turn.Stay : Turn.Hit;
    }
}

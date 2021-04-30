package AI.PossibleMoves;

import President.Game.Player;
import President.Game.Tuple;

import java.util.ArrayList;

public class PresidentPossibleMoves extends PossibleMoves {

    Player player;
    Tuple gameState;

    public PresidentPossibleMoves(Player player, Tuple gameState) {

        this.player = player;
        this.gameState = gameState;
    }

    @Override
    public ArrayList<Tuple> getPossibleActions() {

        ArrayList<Tuple> out = new ArrayList<>();
        for (Tuple tuple : player.getSortedDeck()) {
            if (tuple.getNumber() == 2 && tuple.getOccurrence() > 0) {
                out.add(new Tuple(2, 1));
                continue;
            }
            if (tuple.getNumber() > gameState.getNumber()) {
                int possActionsOnTuple = (tuple.getOccurrence() - gameState.getOccurrence()) + 1;
                if (possActionsOnTuple > 0) {
                    int count = 0;
                    while (count < possActionsOnTuple) {
                        out.add(new Tuple(tuple.getNumber(), gameState.getOccurrence() + count));
                        count++;
                    }
                }
            }
        }
        return out;
    }

    @Override
    public ArrayList<int[][]> getPossibleMoves() {
        return null;
    }
}

package AI.PossibleMoves;

import President.Game.Card;
import President.Game.Tuple;

import java.util.ArrayList;

public abstract class PossibleMoves {

    public abstract ArrayList<int[][]> getPossibleMoves();
    public abstract ArrayList<Tuple> getPossibleActions();
    public abstract ArrayList<Tuple> getInformationSet(ArrayList<Card> deck);
    public abstract ArrayList<Card> computeInformationSetCards();
}

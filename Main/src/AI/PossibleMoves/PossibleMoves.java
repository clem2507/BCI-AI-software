package AI.PossibleMoves;

import President.Game.Tuple;

import java.util.ArrayList;

public abstract class PossibleMoves {

    public abstract ArrayList<int[][]> getPossibleMoves();
    public abstract ArrayList<Tuple> getPossibleActions();
}

package Abalone.Game;

public interface Player {
    Move collectMove() throws InterruptedException;
    void setTurn(int turn);
    void setBoard(BoardUI board);
}

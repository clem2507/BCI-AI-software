package Abalone.Game;

public interface PlayerInterface {
    Move collectMove() throws InterruptedException;
    void setTurn(int turn);
    void setBoard(BoardUI board);
}

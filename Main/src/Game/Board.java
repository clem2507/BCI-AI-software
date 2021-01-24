package Game;

public class Board {

    public int[][] gameBoard;

    public Board(int[][] gameBoard) {

        this.gameBoard = gameBoard;
    }

    public void setBoard(int[][] currentBoard) {
        this.gameBoard = currentBoard;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }
}

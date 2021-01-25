package Game;

public class Board {

    private int[][] gameBoard = new int[][]{

            {2, 0, 2, 0, 2},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {1, 0, 1, 0, 1}

    };

    public void setBoard(int[][] currentBoard) {
        this.gameBoard = currentBoard;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }
}

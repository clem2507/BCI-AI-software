package Game;

public class Move {

    private MoveDirection direction;
    private int[][] currentBoard;
    private int i;
    private int j;
    private int currentPlayer;
    private int opponentPlayer;
    private int[][] newState;


    public Move(int i, int j, MoveDirection move, int[][] currentBoard, int currentPlayer) {

        this.i = i+1;
        this.j = j+1;
        this.direction = move;
        this.currentBoard = currentBoard;
        this.currentPlayer = currentPlayer;

        if (currentPlayer == 1) {
            opponentPlayer = 2;
        }
        else {
            opponentPlayer = 1;
        }
    }

    public boolean checkMove() {

        return checkInSquare(direction);
    }

    public boolean checkInSquare(MoveDirection move) {

        boolean check = false;
        newState = copyBoard(currentBoard);
        newState[i][j] = 0;
        switch (move) {
            case TOP_LEFT:
                if (currentBoard[i-1][j-1] == 0) {
                    newState[i-1][j-1] = currentPlayer;
                    check = true;
                }
                else if (currentBoard[i-1][j-1] == opponentPlayer) {
                    if (currentPlayer == 1) {
                        if (currentBoard[i - 2][j - 2] == 0) {
                            newState[i - 1][j - 1] = 0;
                            newState[i - 2][j - 2] = currentPlayer;
                            check = true;
                        }
                    }
                }
                break;
            case BOTTOM_LEFT:
                if (currentBoard[i+1][j-1] == 0) {
                    newState[i+1][j-1] = currentPlayer;
                    check = true;
                }
                else if (currentBoard[i+1][j-1] == opponentPlayer) {
                    if (currentPlayer == 2) {
                        if (currentBoard[i+2][j-2] == 0) {
                            newState[i+1][j-1] = 0;
                            newState[i+2][j-2] = currentPlayer;
                            check = true;
                        }
                    }
                }
                break;
            case TOP_RIGHT:
                if (currentBoard[i-1][j+1] == 0) {
                    newState[i-1][j+1] = currentPlayer;
                    check = true;
                }
                else if (currentBoard[i-1][j+1] == opponentPlayer) {
                    if (currentPlayer == 1) {
                        if (currentBoard[i - 2][j + 2] == 0) {
                            newState[i - 1][j + 1] = 0;
                            newState[i - 2][j + 2] = currentPlayer;
                            check = true;
                        }
                    }
                }
                break;
            case BOTTOM_RIGHT:
                if (currentBoard[i+1][j+1] == 0) {
                    newState[i+1][j+1] = currentPlayer;
                    check = true;
                }
                else if (currentBoard[i+1][j+1] == opponentPlayer) {
                    if (currentPlayer == 2) {
                        if (currentBoard[i + 2][j + 2] == 0) {
                            newState[i + 1][j + 1] = 0;
                            newState[i + 2][j + 2] = currentPlayer;
                            check = true;
                        }
                    }
                }
                break;
        }
        return check;
    }

    public int[][] copyBoard(int[][] board) {

        int[][] out = new int[board.length][board.length];
        for (int k = 0; k < board.length; k++) {
            for (int l = 0; l < board.length; l++) {
                out[k][l] = board[k][l];
            }
        }
        return out;
    }

    public int[][] getNewState() {
        return newState;
    }
}

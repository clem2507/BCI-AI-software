package Game;

import Output.Test;

public class Move {

    private MoveDirection direction;
    private Board currentBoard;
    private int i;
    private int j;
    private int currentPlayer;
    private int[][] newState;


    public Move(int i, int j, MoveDirection move, Board currentBoard, int currentPlayer) {

        this.i = i;
        this.j = j;
        this.direction = move;
        this.currentBoard = currentBoard;
        this.currentPlayer = currentPlayer;
    }

    public boolean checkMove() {

        boolean check = false;
        if (i > 0 && i < 4 && j > 0 && j < 4) {
            switch (direction) {
                case TOP_LEFT:
                    check = checkInSquare(MoveDirection.TOP_LEFT);
                    break;
                case LEFT:
                    check = checkInSquare(MoveDirection.LEFT);
                    break;
                case BOTTOM_LEFT:
                    check = checkInSquare(MoveDirection.BOTTOM_LEFT);
                    break;
                case TOP_RIGHT:
                    check = checkInSquare(MoveDirection.TOP_RIGHT);
                    break;
                case RIGHT:
                    check = checkInSquare(MoveDirection.RIGHT);
                    break;
                case BOTTOM_RIGHT:
                    check = checkInSquare(MoveDirection.BOTTOM_RIGHT);
                    break;
                case FORWARD:
                    check = checkInSquare(MoveDirection.FORWARD);
                    break;
                case BACKWARD:
                    check = checkInSquare(MoveDirection.BACKWARD);
                    break;
            }
        }
        else if (i == 0 && j > 0 && j < 4) {
            switch (direction) {
                case LEFT:
                    check = checkInSquare(MoveDirection.LEFT);
                    break;
                case BOTTOM_LEFT:
                    check = checkInSquare(MoveDirection.BOTTOM_LEFT);
                    break;
                case BACKWARD:
                    check = checkInSquare(MoveDirection.BACKWARD);
                    break;
                case BOTTOM_RIGHT:
                    check = checkInSquare(MoveDirection.BOTTOM_RIGHT);
                    break;
                case RIGHT:
                    check = checkInSquare(MoveDirection.RIGHT);
                    break;
            }
        }
        else if (i > 0 && i < 4 && j == 4) {
            switch (direction) {
                case LEFT:
                    check = checkInSquare(MoveDirection.LEFT);
                    break;
                case BOTTOM_LEFT:
                    check = checkInSquare(MoveDirection.BOTTOM_LEFT);
                    break;
                case BACKWARD:
                    check = checkInSquare(MoveDirection.BACKWARD);
                    break;
                case TOP_LEFT:
                    check = checkInSquare(MoveDirection.TOP_LEFT);
                    break;
                case FORWARD:
                    check = checkInSquare(MoveDirection.FORWARD);
                    break;
            }
        }
        else if (i == 4 && j > 0 && j < 4) {
            switch (direction) {
                case LEFT:
                    check = checkInSquare(MoveDirection.LEFT);
                    break;
                case TOP_LEFT:
                    check = checkInSquare(MoveDirection.TOP_LEFT);
                    break;
                case FORWARD:
                    check = checkInSquare(MoveDirection.FORWARD);
                    break;
                case TOP_RIGHT:
                    check = checkInSquare(MoveDirection.TOP_RIGHT);
                    break;
                case RIGHT:
                    check = checkInSquare(MoveDirection.RIGHT);
                    break;
            }
        }
        else if (i > 0 && i < 4 && j == 0) {
            switch (direction) {
                case FORWARD:
                    check = checkInSquare(MoveDirection.FORWARD);
                    break;
                case TOP_RIGHT:
                    check = checkInSquare(MoveDirection.TOP_RIGHT);
                    break;
                case RIGHT:
                    check = checkInSquare(MoveDirection.RIGHT);
                    break;
                case BOTTOM_RIGHT:
                    check = checkInSquare(MoveDirection.BOTTOM_RIGHT);
                    break;
                case BACKWARD:
                    check = checkInSquare(MoveDirection.BACKWARD);
                    break;
            }
        }
        else if (i == 0 && j == 0) {
            switch (direction) {
                case BACKWARD:
                    check = checkInSquare(MoveDirection.BACKWARD);
                    break;
                case BOTTOM_RIGHT:
                    check = checkInSquare(MoveDirection.BOTTOM_RIGHT);
                    break;
                case RIGHT:
                    check = checkInSquare(MoveDirection.RIGHT);
                    break;
            }
        }
        else if (i == 0 && j == 4) {
            switch (direction) {
                case LEFT:
                    check = checkInSquare(MoveDirection.LEFT);
                    break;
                case BOTTOM_LEFT:
                    check = checkInSquare(MoveDirection.BOTTOM_LEFT);
                    break;
                case BACKWARD:
                    check = checkInSquare(MoveDirection.BACKWARD);
                    break;
            }
        }
        else if (i == 4 && j == 4) {
            switch (direction) {
                case LEFT:
                    check = checkInSquare(MoveDirection.LEFT);
                    break;
                case TOP_LEFT:
                    check = checkInSquare(MoveDirection.TOP_LEFT);
                    break;
                case FORWARD:
                    check = checkInSquare(MoveDirection.FORWARD);
                    break;
            }
        }
        else {
            switch (direction) {
                case RIGHT:
                    check = checkInSquare(MoveDirection.RIGHT);
                    break;
                case TOP_RIGHT:
                    check = checkInSquare(MoveDirection.TOP_RIGHT);
                    break;
                case FORWARD:
                    check = checkInSquare(MoveDirection.FORWARD);
                    break;
            }
        }
        return check;
    }

    public boolean checkInSquare(MoveDirection move) {

        boolean check = false;
        newState = currentBoard.getGameBoard();
        newState[i][j] = 0;
        switch (move) {
            case TOP_LEFT:
                if (currentBoard.getGameBoard()[i-1][j-1] == 0) {
                    newState[i-1][j-1] = currentPlayer;
                    check = true;
                }
                break;
            case LEFT:
                if (currentBoard.getGameBoard()[i][j-1] == 0) {
                    newState[i][j-1] = currentPlayer;
                    check = true;
                }
                break;
            case BOTTOM_LEFT:
                if (currentBoard.getGameBoard()[i+1][j-1] == 0) {
                    newState[i+1][j-1] = currentPlayer;
                    check = true;
                }
                break;
            case TOP_RIGHT:
                if (currentBoard.getGameBoard()[i-1][j+1] == 0) {
                    newState[i-1][j+1] = currentPlayer;
                    check = true;
                }
                break;
            case RIGHT:
                if (currentBoard.getGameBoard()[i][j+1] == 0) {
                    newState[i][j+1] = currentPlayer;
                    check = true;
                }
                break;
            case BOTTOM_RIGHT:
                if (currentBoard.getGameBoard()[i+1][j+1] == 0) {
                    newState[i+1][j+1] = currentPlayer;
                    check = true;
                }
                break;
            case FORWARD:
                // TODO: carry the pushing move
                if (currentBoard.getGameBoard()[i-1][j] == 0) {
                    newState[i-1][j] = currentPlayer;
                    check = true;
                }
                break;
            case BACKWARD:
                // TODO: carry the pushing move
                if (currentBoard.getGameBoard()[i+1][j] == 0) {
                    newState[i+1][j] = currentPlayer;
                    check = true;
                }
                break;
        }
        return check;
    }

    public void makeMove() {

        int[][] newBoard = currentBoard.getGameBoard();
        newBoard[i][j] = 0;
        switch (direction) {
            case TOP_LEFT:
                newBoard[i-1][j-1] = currentPlayer;
                break;
            case LEFT:
                newBoard[i][j-1] = currentPlayer;
                break;
            case BOTTOM_LEFT:
                newBoard[i+1][j-1] = currentPlayer;
                break;
            case TOP_RIGHT:
                newBoard[i-1][j+1] = currentPlayer;
                break;
            case RIGHT:
                newBoard[i][j+1] = currentPlayer;
                break;
            case BOTTOM_RIGHT:
                newBoard[i+1][j+1] = currentPlayer;
                break;
            case FORWARD:
                newBoard[i-1][j] = currentPlayer;
                break;
            case BACKWARD:
                newBoard[i+1][j] = currentPlayer;
                break;
        }
        currentBoard.setBoard(newBoard);
    }

    public int[][] getNewState() {
        return newState;
    }
}

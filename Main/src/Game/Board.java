package Game;

import GUI.GameUI;
import GUI.Marble;
import Output.Test;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Board {

    public Marble[][] marbles;
    private int numberOfMarblesOnBoard;

    public boolean[][] selected;

    public boolean isSelected;
    public int xSelected;
    public int ySelected;

    private Light.Point light;
    private Lighting lighting;

    private int[][] gameBoard;

    public Board(int numberOfMarblesOnBoard) {

        this.numberOfMarblesOnBoard = numberOfMarblesOnBoard;
        this.gameBoard = createBoardArray(numberOfMarblesOnBoard);
        this.selected = new boolean[gameBoard.length-2][gameBoard.length-2];

        light = new Light.Point();
        light.setColor(Color.WHITE);
        light.setX(70);
        light.setY(55);
        light.setZ(45);
        lighting = new Lighting();
        lighting.setLight(light);

        createCircles();
        drawAllMarbles();
    }

    public void createCircles() {

        marbles = new Marble[gameBoard.length-2][gameBoard.length-2];
        for (int i = 0; i < marbles.length; i++) {
            for (int j = 0; j < marbles.length; j++) {
                Marble marble = new Marble(25);
                marble.setCenterX((j+1.5)*GameUI.squareSize);
                marble.setCenterY((i+1.5)*GameUI.squareSize);
                marble.setStrokeWidth(2);
                marbles[i][j] = marble;

                marble.x = i;
                marble.y = j;

                int finalI = i;
                int finalJ = j;
                marble.setOnMouseClicked(e -> {
                    if (gameBoard[marble.x+1][marble.y+1] == Checkers.currentPlayer && !selected[marble.x][marble.y] && countSelected() < 1) {
                        selected[marble.x][marble.y] = true;
                        isSelected = true;
                        xSelected = finalI;
                        ySelected = finalJ;
                        drawPossibleMoves(false);
                        drawAllMarbles();
                    } else if (selected[marble.x][marble.y] && gameBoard[marble.x+1][marble.y+1] == Checkers.currentPlayer) {
                        selected[marble.x][marble.y] = false;
                        isSelected = false;
                        drawPossibleMoves(true);
                        drawAllMarbles();
                    }
                });
            }
        }
    }

    public void drawAllMarbles() {

        for (int i = 0; i < marbles.length; i++) {
            for (int j = 0; j < marbles.length; j++) {
                drawMarble(i, j);
            }
        }
    }

    public void drawMarble(int i, int j) {

        Color color = null;
        switch (gameBoard[i+1][j+1]) {
            case 0:
                if (i%2 == 0) {
                    if (j % 2 == 0) {
                        color = Color.rgb(255, 255, 255, 1.0);
                    }
                    else {
                        color = Color.rgb(200, 200, 200, 1.0);
                    }
                    marbles[i][j].setEffect(null);
                }
                else {
                    if (j % 2 == 0) {
                        color = Color.rgb(200, 200, 200, 1.0);
                    }
                    else {
                        color = Color.rgb(255, 255, 255, 1.0);
                    }
                    marbles[i][j].setEffect(null);
                }
                break;
            case 1:
                color = Color.rgb(255, 255, 255, 1.0);
                marbles[i][j].setEffect(lighting);
                break;
            case 2:
                color = Color.rgb(1, 1, 1, 1.0);
                marbles[i][j].setEffect(lighting);
                break;
        }
        if (selected[i][j]) {
            color = Color.rgb(200, 1, 1, 1.0);
        }
        if (color != null) {
            marbles[i][j].setFill(color);
        }
    }

    public void drawPossibleMoves(boolean clear) {

        PossibleMoves possibleMoves = new PossibleMoves(gameBoard, Checkers.currentPlayer);
        ArrayList<int[][]> movesArray = possibleMoves.getPossibleMovesFromMarble(xSelected+1, ySelected+1);
        for (int[][] board : movesArray) {
            for (int k = 1; k < board.length-1; k++) {
                for (int l = 1; l < board.length-1; l++) {
                    if (board[k][l] != gameBoard[k][l]) {
                        if (!clear) {
                            marbles[k-1][l-1].setStroke(Color.rgb(200, 1, 1, 1.0));
                        }
                        else {
                            marbles[xSelected][ySelected].setStroke(null);
                            marbles[k-1][l-1].setStroke(null);
                        }
                    }
                }
            }
        }
    }

    public void clearStrokes() {

        for (int i = 0; i < marbles.length; i++) {
            for (int j = 0; j < marbles.length; j++) {
                if (gameBoard[i+1][j+1] == 0) {
                    marbles[i][j].setStroke(null);
                }
            }
        }
    }

    public int[][] createBoardArray(int size) {

        int[][] out = new int[size+2][size+2];
        for (int i = 0; i < size+2; i++) {
            for (int j = 0; j < size+2; j++) {
                if (i==0 || j==size+1 || j==0 || i==size+1) {
                    out[i][j] = 3;
                }
                else if (i == 1 && j%2==0) {
                    out[i][j] = 2;
                }
                else if (i == size && j%2!=0) {
                    out[i][j] = 1;
                }
            }
        }
        return out;
    }

    public int countSelected(){

        int selectedCounter = 0;
        for(int i = 0; i < selected.length; i++) {
            for(int j = 0; j < selected.length; j++) {
                if(selected[i][j]) {
                    selectedCounter++;
                }
            }
        }
        return selectedCounter;
    }

    public void setBoard(int[][] currentBoard) {
        this.gameBoard = currentBoard;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public boolean[][] getSelected() {
        return selected;
    }
}

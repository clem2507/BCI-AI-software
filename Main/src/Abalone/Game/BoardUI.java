package Abalone.Game;

import AI.Util;
import Abalone.GUI.Marble;
import Checkers.Game.Checkers;
import javafx.scene.effect.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

import static javafx.scene.paint.Color.*;

/**
 * This class takes care of the UI by defining the look of the marbles, the holes and the board
 */

public class BoardUI {

    private int[][] cellColors; // each cell of the board is associated to an integer which defines whether it
    private boolean[][] selected;
    public boolean isSelected;
    public boolean isSelectable = true;

    // is a marble, a hole or out of the board
    public Marble[][] circles; // array of all the Circles of the board (might be a hole or a marble)

    public Polygon hexagon; // the board shaped like an hexagon

    private final double RADIUS = 22; // radius of the circles (marbles & holes)

    public BoardUI(){
        hexagon = createHexagon();

        createColors();
        createCircles();
        drawAllCells();
    }

    public int[][] getBoard() {
        return cellColors;
    }

    public void setBoard(int[][] newBoard) {
        this.cellColors = newBoard;
    }

    /**
     * Creates the board itself shaped like a hexagon
     * @return the hexagon (representing the board)
     */
    public static Polygon createHexagon() {
        Polygon hexagon = new Polygon();

        //Adding coordinates to the hexagon
        hexagon.getPoints().addAll(
                315.0, 90.0, //1
                540.0, 90.0, //1
                720.0, 315.0, //2
                540.0, 550.0, //3
                315.0, 550.0, //3
                150.0, 315.0 //2
        );

        //define the color of the hexagon
        Color hexagonColor = GREY;
        hexagon.setFill(hexagonColor);

        //Shadow effect on hexagon:
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(hexagonColor.darker());
        dropShadow.setRadius(3);
        dropShadow.setOffsetX(2);
        dropShadow.setOffsetY(2);
        dropShadow.setSpread(20);
        hexagon.setEffect(dropShadow);

        return hexagon;
    }

    public int[][] getSelected() {
        int[][] ans;
        int cnt = 0;
        for (int i = 0; i < cellColors.length; i++) {
            for (int j = 0; j < cellColors[0].length; j++) {
                if (selected[i][j]) {
                    cnt++;
                }
            }
        }

        ans = new int[cnt][2];
        cnt = 0;
        for (int i = 0; i < cellColors.length; i++) {
            for (int j = 0; j < cellColors[0].length; j++) {
                if (selected[i][j]) {
                    ans[cnt][0] = i;
                    ans[cnt][1] = j;
                    cnt++;
                }
            }
        }

        return ans;
    }

    public void unselect() {
        for (int i = 0; i < cellColors.length; i++) {
            for (int j = 0; j < cellColors[0].length; j++) {
                selected[i][j] = false;
            }
        }
    }

    /**
     * Create all circles of the board (a circle can be either a hole, or a marble)
     */
    private void createCircles() {
        circles = new Marble[cellColors.length][cellColors[0].length];
        for (int i = 0; i < cellColors.length; i++)
            for (int j = 0; j < cellColors[0].length; j++)
                circles[i][j] = null;

        selected = new boolean[cellColors.length][cellColors[0].length];
        for (int i = 0; i < cellColors.length; i++)
            for (int j = 0; j < cellColors[0].length; j++)
                selected[i][j] = false;

        int nc = 3; // nc: number of circles
        double x_coord = 490;
        double y_coord = 125;

        for (int i = 0; i < cellColors.length; i++) { // 9 loops for the 9 levels of the hexagon
            for (int j = 0; j < nc; j++) {
                Marble circle = new Marble(RADIUS);
                circle.setCenterX(x_coord);
                circle.setCenterY(y_coord);
                circle.x = i;
                circle.y = j;

                circles[i][j] = circle;
                x_coord += RADIUS * 2 + 35;
                drawCell(i,j);

                // add light effect on marbles
                if(circle.getFill() == WHITE || circle.getFill() == BLACK){
                    Light.Point light = new Light.Point(); //point of light on marbles
                    light.setColor(Color.WHITE); // color of the light
                    //Setting the position of the light
                    light.setX(70);
                    light.setY(55);
                    light.setZ(45);
                    Lighting lighting = new Lighting();
                    lighting.setLight(light);
                    //Applying the Lighting effect to the circle
                    circle.setEffect(lighting);

                }else if(circle.getFill() == GREY){ //add shadow effect on holes
                    InnerShadow innerShadow = new InnerShadow();
                    innerShadow.setOffsetX(4);
                    innerShadow.setOffsetY(4);
                    innerShadow.setColor(Color.GRAY); //color of the shadow
                    circle.setEffect(innerShadow);
                }
//                int finalI = i;
//                int finalJ = j;
                circle.setOnMouseClicked(e -> {
                    if (cellColors[circle.x][circle.y] == Abalone.currentPlayer && !selected[circle.x][circle.y] && countSelected() < 3 && isSelectable) {
                        selected[circle.x][circle.y] = true;
                        isSelected = true;
//                        xSelected = finalI;
//                        ySelected = finalJ;
//                        drawPossibleMoves(false);
                        drawAllCells();
                    } else if (selected[circle.x][circle.y] && cellColors[circle.x][circle.y] == Abalone.currentPlayer && isSelectable) {
                        selected[circle.x][circle.y] = false;
                        isSelected = false;
//                        drawPossibleMoves(true);
                        drawAllCells();
                    }
                });
            }
            // update the number of circles per level
            if (i < 4) { // less than 9 holes at that level
                nc = nc + 1; // increase number of holes
            } else {
                nc = nc - 1; // diminish number of holes
            }

            // update y_coord
            y_coord += RADIUS * 2 + 4;

            //update x_coord
            if (i == 0 || i == 6) {
                x_coord = 455;
            } else if (i == 1 || i == 5) {
                x_coord = 420;
            } else if (i == 2 || i == 4) {
                x_coord = 385;
            } else if (i == 3) {
                x_coord = 352;
            } else { // i==7
                x_coord = 490;
            }
        }
    }

    /**
     * Defines which circle is empty (0) (=hole, in bisque), medium_blue (1) or light_sky_blue (2)
     * Medium_blue represents the marbles of player 1
     * Light_sky_blue represents the marbles of player 2
     * -1 is for "out of the board"
     */

    private void createColors() {
        cellColors = new int[][]{
                {2, 2, 2, -1, -1, -1, -1},
                {2, 2, 2, 2, -1, -1, -1},
                {0, 2, 2, 2,  0, -1, -1},
                {0, 0, 0, 0,  0,  0, -1},
                {0, 0, 0, 0,  0,  0, 0},
                {0, 0, 0, 0,  0,  0, -1},
                {0, 1, 1, 1,  0, -1, -1},
                {1, 1, 1, 1, -1, -1, -1},
                {1, 1, 1, -1, -1, -1, -1}
        };
    }

    /**
     * Colours a circle of the board in its color
     * @param i the index of the circle
     * @param j the second index of the circle
     */
    private void drawCell(int i, int j) {

        Color c = null;
        switch (cellColors[i][j]) {
            case 0:  c = Color.rgb(250,250,250,0.3);       break;
            case 1:  c = BLACK;   break;
            case 2:  c = WHITE; break;
            default: break;
        }
        if (selected[i][j]) {
            c = RED;
        }

        if (c != null) {
            circles[i][j].setFill(c);
            if(c == WHITE || c == BLACK || c == RED){
                Light.Point light = new Light.Point(); //point of light on marbles
                light.setColor(Color.WHITE); // color of the light
                //Setting the position of the light
                light.setX(70);
                light.setY(55);
                light.setZ(45);
                Lighting lighting = new Lighting();
                lighting.setLight(light);
                //Applying the Lighting effect to the circle
                circles[i][j].setEffect(lighting);
            }
            else {
                circles[i][j].setEffect(null);
            }
        }
    }

    /**
     * Colours all the circles of the board
     */
    public void drawAllCells() {
        for (int i = 0; i < cellColors.length; i++) {
            for (int j = 0; j < cellColors[0].length; j++) {
                drawCell(i, j);
            }
        }
    }

    /**
     * Checks whether a marble is selected or not
     * @param i index of the circle (=marble)
     * @param j second index of the circle (=marble)
     * @return boolean true if the marble selected and false if the marble is not selected
     */
    private boolean isSelected(int i, int j){
        return selected[i][j];
    }

    public int countSelected(){

        int selectedCounter = 0;
        for(int i = 0; i< selected.length; i++){
            for(int j = 0; j<selected[0].length; j++){
                if(selected[i][j]){
                    selectedCounter ++;
                }
            }
        }
        return selectedCounter;
    }

    public void drawPossibleMoveFromAI(int[][] board, boolean clear) {

        int opponentPlayer = Util.changeCurrentPlayer(Abalone.currentPlayer);
        ArrayList<int[]> locationPlayerToZero = new ArrayList<>();
        ArrayList<int[]> locationZeroToPlayer = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != cellColors[i][j]) {
                    if (!clear) {
                        if (board[i][j] != 0) {
                            circles[i][j].setStroke(Color.RED);
                            locationZeroToPlayer.add(new int[]{i,j});
                        } else {
                            if (cellColors[i][j] != opponentPlayer) {
                                circles[i][j].setFill(Color.RED);
                                locationPlayerToZero.add(new int[]{i,j});
                            }
                        }
                    }
                    else {
                        circles[i][j].setStroke(null);
                        drawAllCells();
                    }
                }
            }
        }
        if (!clear) {
            if (locationPlayerToZero.size() < 2 && locationZeroToPlayer.size() <= 2) {
                if (Math.abs(locationPlayerToZero.get(0)[0] - locationZeroToPlayer.get(0)[0]) > 1 ||
                        Math.abs(locationPlayerToZero.get(0)[1] - locationZeroToPlayer.get(0)[1]) > 1) {
                    for (int[] coord : findCoordinatesBetweenTwoPoints(locationPlayerToZero, locationZeroToPlayer)) {
                        circles[coord[0]][coord[1]].setFill(RED);
                    }
                }
            }
        }
    }

    public ArrayList<int[]> findCoordinatesBetweenTwoPoints(ArrayList<int[]> startList, ArrayList<int[]> endList) {

        int[] start = startList.get(0);
        int[] end;
        if (endList.size() == 1) {
            end = endList.get(0);
        }
        else {
            if (Math.abs(start[0] - endList.get(0)[0]) < Math.abs(start[0] - endList.get(1)[0])) {
                end = endList.get(0);
            }
            else {
                end = endList.get(1);
            }
        }
        ArrayList<int[]> out = new ArrayList<>();
        int option = 0;
        if ((start[0] <= 4 && end[0] <= 4) || (start[0] >= 4 && end[0] >= 4)) {
            option = 1;
        }
        else if (start[0] > 4 && end[0] < 4) {
            option = 2;
        }
        else if (start[0] < 4 && end[0] > 4) {
            option = 3;
        }
        for (int i = 0; i < Math.max(Math.abs(start[0]-end[0]), Math.abs(start[1]-end[1]))-1; i++) {
            switch (option) {
                case 1:
                    option1coord(out, i, start, end);
                    break;
                case 2:
                    option2coord(out, i, start, end);
                    break;
                case 3:
                    option3coord(out, i, start, end);
                    break;
            }
        }
        return out;
    }

    private void option1coord(ArrayList<int[]> out, int i, int[] start, int[] end) {

        int[] coord = new int[2];
        if (start[0] - end[0] != 0 && start[1] - end[1] != 0) {
            if (start[0] < end[0] && start[1] < end[1]) {
                coord[0] = start[0] + (i+1);
                coord[1] = start[1] + (i+1);
            } else if (start[0] > end[0] && start[1] > end[1]) {
                coord[0] = start[0] - (i+1);
                coord[1] = start[1] - (i+1);
            } else if (start[0] > end[0] && start[1] < end[1]) {
                coord[0] = start[0] - (i+1);
                coord[1] = start[1] + (i+1);
            } else if (start[0] < end[0] && start[1] > end[1]) {
                coord[0] = start[0] + (i+1);
                coord[1] = start[1] - (i+1);
            }
        } else {
            if (start[0] - end[0] == 0) {
                coord[0] = start[0];
                coord[1] = Math.min(start[1], end[1]) + (i+1);
            } else {
                coord[0] = Math.min(start[0], end[0]) + (i+1);
                coord[1] = start[1];
            }
        }
        out.add(coord);
    }

    private void option2coord(ArrayList<int[]> out, int i, int[] start, int[] end) {

        int[] coord = new int[2];
        if (start[1] > end[1]) {
            if (start[0]-(i+1) >= 4) {
                coord[0] = start[0] - (i+1);
                coord[1] = start[1];
            }
            else {
                coord[0] = out.get(i-1)[0] - 1;
                coord[1] = out.get(i-1)[1] - 1;
            }
        }
        else {
            if (start[0]-(i+1) >= 4) {
                coord[0] = start[0] - (i+1);
                coord[1] = start[1] + (i+1);
            }
            else {
                coord[0] = out.get(i-1)[0] - 1;
                coord[1] = out.get(i-1)[1];
            }
        }
        out.add(coord);
    }

    private void option3coord(ArrayList<int[]> out, int i, int[] start, int[] end) {

        int[] coord = new int[2];
        if (start[1] < end[1]) {
            if (start[0]+(i+1) <= 4) {
                coord[0] = start[0] + (i+1);
                coord[1] = start[1] + (i+1);
            }
            else {
                coord[0] = out.get(i-1)[0] + 1;
                coord[1] = out.get(i-1)[1];
            }
        }
        else {
            if (start[0]+(i+1) <= 4) {
                coord[0] = start[0] + (i+1);
                coord[1] = start[1];
            }
            else {
                coord[0] = out.get(i-1)[0] + 1;
                coord[1] = out.get(i-1)[1] - 1;
            }
        }
        out.add(coord);
    }

    public void clearStrokes() {

        for (int i = 0; i < circles.length; i++) {
            for (int j = 0; j < circles[0].length; j++) {
                if (cellColors[i][j]!=-1) {
                    circles[i][j].setStroke(null);
                }
            }
        }
    }

    public Marble[][] getCircles() {
        return circles;
    }

    public int[][] getGameBoard() {
        return cellColors;
    }
}

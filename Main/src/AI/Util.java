package AI;

import java.io.*;
import java.util.Random;

public class Util {

    /**
     * static method used to change the current number of the player
     * @param currentPlayer value to change
     * @return new player state value
     */
    public static int changeCurrentPlayer(int currentPlayer) {

        if (currentPlayer == 1) {
            return 2;
        }
        else {
            return 1;
        }
    }

    /**
     * static method to count the amount of marbles on board for a given player
     * @param board to be based on
     * @param player to count marbles on
     * @return the number
     */
    public static int countMarbles(int[][] board, int player) {

        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j]==player) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * static method to print a 2D integer board in the terminal
     * @param board to print
     */
    public static void printBoard(int[][] board) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * static method to check if 2 boards are completely equal or not
     * @param board1 to compare
     * @param board2 to compare
     * @return true if they are equal
     */
    public static boolean isEqual(int[][] board1, int[][] board2) {

        for (int i = 0; i < board1.length; i++) {
            for (int j = 0; j < board1.length; j++) {
                if (board1[i][j] != board2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * static method to generate a random number following a normal distribution
     * @param mean of the distribution
     * @param variance of the distribution
     * @return the number generated
     */
    public static double getGaussian(double mean, double variance){

        Random fRandom = new Random();
        return mean + fRandom.nextGaussian() * variance;
    }

    /**
     * static method the average of the values in an array
     * @param arr input
     * @return the average
     */
    public static double getArrayAverage(double[] arr) {

        double sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum+=arr[i];
        }
        return sum/arr.length;
    }

    /**
     * update the name adaptive variable values
     * @param path to the file names
     * @param name of the player
     * @param adaptiveVar of the current action
     */
    public static void updateNameAdaptiveVar(String path, String name, double adaptiveVar) {

        try {
            BufferedReader file = new BufferedReader(new FileReader(path));
            String line = file.readLine();
            StringBuilder temp = new StringBuilder();
            int lineNumber = 0;
            while (line != null) {
                if (name.equals(line.split(", ")[0])) {
                    if (line.split(", ").length > 100) {
                        int count = 0;
                        for (String word : line.split(", ")) {
                            if (count != 1) {
                                temp.append(word);
                                temp.append(", ");
                            }
                            count++;
                        }
                    }
                    else {
                        for (String word : line.split(", ")) {
                            temp.append(word);
                            temp.append(", ");
                        }
                    }
                    temp.append(adaptiveVar);
                    temp.append(", ");
                    StringBuffer buffer = new StringBuffer();
                    int count = 0;
                    BufferedReader br = new BufferedReader(new FileReader(path));
                    line = br.readLine();
                    while (line != null) {
                        if (count != lineNumber) {
                            buffer.append(line).append(System.lineSeparator());
                        }
                        else {
                            buffer.append(temp).append(System.lineSeparator());
                        }
                        line = br.readLine();
                        count++;
                    }
                    PrintWriter writer = new PrintWriter(path);
                    writer.print("");
                    String fileContents = buffer.toString();
                    writer.append(fileContents);
                    writer.flush();
                    break;
                }
                line = file.readLine();
                lineNumber++;
            }
        } catch (IOException e) {
            System.out.println("Problem reading file.");
            e.printStackTrace();
        }
    }

    /**
     * method to get the adaptive variable of a player username
     * @param path to the file names
     * @param name of the player
     * @return the adaptive variable of the corresponding player
     */
    public static double getNameAdaptiveVariable(String path, String name) {

        double sum = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                if (line.split(", ")[0].equals(name)) {
                    int count = 0;
                    for (String word : line.split(", ")) {
                        if (count > 0) {
                            sum += Double.parseDouble(word);
                        }
                        count++;
                    }
                    sum/=(line.split(", ").length-1);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Problem reading file.");
            e.printStackTrace();
        }
        return sum;
    }
}

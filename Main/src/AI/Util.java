package AI;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Util {

    public static int changeCurrentPlayer(int currentPlayer) {

        if (currentPlayer == 1) {
            return 2;
        }
        else {
            return 1;
        }
    }

    public static boolean changeCurrentPlayer(boolean currentPlayer) {

        if (currentPlayer) {
            return false;
        }
        else {
            return true;
        }
    }

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

    public static void printBoard(int[][] board) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

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

    public static double getGaussian(double aMean, double aVariance){

        Random fRandom = new Random();
        return aMean + fRandom.nextGaussian() * aVariance;
    }

    public static double getArrayAverage(double[] arr) {

        double sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum+=arr[i];
        }
        return sum/arr.length;
    }

    public static void updateNameAdaptiveVar(String path, String name, double adaptiveVar) {

        try {
            BufferedReader file = new BufferedReader(new FileReader(path));
            String line = file.readLine();
            StringBuilder temp = new StringBuilder();
            int lineNumber = 0;
            while (line != null) {
//                System.out.println("line = " + line);
//                System.out.println((line.split(", ")[0]));
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
//                    Scanner sc = new Scanner(new File(path));
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
//                    fileContents = fileContents.replaceAll(oldLine, String.valueOf(temp));
//                    FileWriter writer = new FileWriter(path);
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

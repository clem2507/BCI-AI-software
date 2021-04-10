package Output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Deals with the simulations' results and outputs them to a CSV file
 */
public class OutputCSV {

    private String fileName;
    private String stringHeaderResume;

    private String filePath;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private PrintWriter printWriter;


    public OutputCSV(String fileName, String headerResume) {

        this.fileName = fileName;
        this.stringHeaderResume = headerResume;

        this.filePath = fileName;
        try {
            this.fileWriter = new FileWriter(filePath,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bufferedWriter = new BufferedWriter(fileWriter);
        this.printWriter = new PrintWriter(bufferedWriter);
        this.printWriter.println(stringHeaderResume);
    }

    public OutputCSV(String fileName) {

        this.fileName = fileName;

        this.filePath = fileName;
        try {
            this.fileWriter = new FileWriter(filePath,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bufferedWriter = new BufferedWriter(fileWriter);
        this.printWriter = new PrintWriter(bufferedWriter);
    }

    public void writeResume(String[][] data) {

        try {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    printWriter.print(data[i][j] + ", ");
                }
                printWriter.println();
            }
            printWriter.flush();
            printWriter.close();
            System.out.println();
            System.out.println("Data is save in: " + fileName);
            System.out.println();
        }
        catch (Exception e) {
            System.out.println("Recorded not save");
        }
    }
}



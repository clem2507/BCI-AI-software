package President.Game;

import AI.PossibleMoves.PossibleMoves;
import AI.PossibleMoves.PresidentPossibleMoves;

import java.util.ArrayList;
import java.util.Scanner;

public class TestGame {

    public static void main(String[]args) {

        int turnCounter = 1;
        President president = new President();
        while (!president.isDone(president.getPlayer1(), president.getPlayer2())) {
            boolean flag = false;
            System.out.println();
            System.out.println("----------------------------");
            System.out.println();
            System.out.println("Turn counter = " + turnCounter);
            System.out.println("Current game state = " + president.getCurrentGameState().getNumber() + " -> " + president.getCurrentGameState().getOccurrence());
            if (president.getPlayer1().toPlay()) {
                System.out.println();
                System.out.println("Player 1 deck sorted:");
                System.out.println();
                for (Tuple tuple : president.getPlayer1().getSortedDeck()) {
                    System.out.println(tuple.getNumber() + " -> " + tuple.getOccurrence());
                }
                System.out.println();
                System.out.println("Please pick a correct available combination of cards shown below:");
                System.out.println();
                PossibleMoves possibleMoves = new PresidentPossibleMoves(president.getPlayer1(), president.getCurrentGameState());
                for (Tuple tuple : possibleMoves.getPossibleActions()) {
                    System.out.println(tuple.getNumber() + " -> " + tuple.getOccurrence());
                }
                System.out.println();
                Scanner scanner = new Scanner(System.in);
                int cardNumber = 0;
                int cardOccurrence = 0;
                while (!flag) {
                    System.out.println("Enter a valid card number:");
                    cardNumber = scanner.nextInt();
                    System.out.println("Enter a valid card occurrence:");
                    cardOccurrence = scanner.nextInt();
                    if (checkInDeck(possibleMoves.getPossibleActions(), cardNumber, cardOccurrence)) {
                        flag = true;
                    }
                }
                president.getPlayer1().takeAction(cardNumber, cardOccurrence);
                if (cardNumber==2) {
                    president.setGameState(new Tuple(0, 0));
                    president.getPlayer1().isToPlay(true);
                    president.getPlayer2().isToPlay(false);
                }
                else {
                    president.setGameState(new Tuple(cardNumber, cardOccurrence));
                    president.getPlayer1().isToPlay(false);
                    president.getPlayer2().isToPlay(true);
                }
            }
            else if (president.getPlayer2().toPlay()) {
                System.out.println();
                System.out.println("Player 2 deck sorted:");
                System.out.println();
                for (Tuple tuple : president.getPlayer2().getSortedDeck()) {
                    System.out.println(tuple.getNumber() + " -> " + tuple.getOccurrence());
                }
                System.out.println();
                System.out.println("Please pick a correct combination of cards shown below:");
                System.out.println();
                PossibleMoves possibleMoves = new PresidentPossibleMoves(president.getPlayer2(), president.getCurrentGameState());
                for (Tuple tuple : possibleMoves.getPossibleActions()) {
                    System.out.println(tuple.getNumber() + " -> " + tuple.getOccurrence());
                }
                System.out.println();
                Scanner scanner = new Scanner(System.in);
                int cardNumber = 0;
                int cardOccurrence = 0;
                while (!flag) {
                    System.out.println("Enter a valid card number:");
                    cardNumber = scanner.nextInt();
                    System.out.println("Enter a valid card occurrence:");
                    cardOccurrence = scanner.nextInt();
                    if (checkInDeck(possibleMoves.getPossibleActions(), cardNumber, cardOccurrence)) {
                        flag = true;
                    }
                }
                president.getPlayer2().takeAction(cardNumber, cardOccurrence);
                if (cardNumber==2) {
                    president.setGameState(new Tuple(0, 0));
                    president.getPlayer1().isToPlay(false);
                    president.getPlayer2().isToPlay(true);
                }
                else {
                    president.setGameState(new Tuple(cardNumber, cardOccurrence));
                    president.getPlayer1().isToPlay(true);
                    president.getPlayer2().isToPlay(false);
                }
            }
            turnCounter++;
        }
    }

    public static boolean checkInDeck(ArrayList<Tuple> possibleActions, int cardNumber, int cardOccurrence) {

        for (Tuple tuple : possibleActions) {
            if (tuple.getNumber() == cardNumber && tuple.getOccurrence() == cardOccurrence) {
                return true;
            }
        }
        return false;
    }
}

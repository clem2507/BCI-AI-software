package President.Game;

import javafx.scene.shape.Rectangle;

public class Card extends Rectangle {

    private int number;
    private int symbol;

    public Card(int number, int symbol) {

        this.number = number;
        this.symbol = symbol;
    }

    public int getNumber() {
        return number;
    }

    public int getSymbol() {
        return symbol;
    }
}

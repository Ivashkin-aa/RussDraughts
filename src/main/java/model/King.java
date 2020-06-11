package model;

import javafx.scene.paint.Color;

public class King extends Figure {

    public King(int x, int y, boolean color) {
        super(x, y, color);
        this.cir.setStroke(Color.YELLOW);
    }
}

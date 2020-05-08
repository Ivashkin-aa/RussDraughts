package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.Main;

public class Cub {
    private Figure figure;

    public Cub(int x, int y, boolean white) {
        Rectangle rec = new Rectangle();
        rec.setX(x * Main.size);
        rec.setY(y * Main.size);
        rec.setWidth(Main.size);
        rec.setHeight(Main.size);
        rec.setFill(white ? Color.valueOf("#c8c8c8") : Color.valueOf("#646464"));
        Main.root.getChildren().add(rec);
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }
    public Figure getFigure() {
        return figure;
    }

}

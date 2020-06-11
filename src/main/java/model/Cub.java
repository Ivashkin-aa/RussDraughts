package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.Main;

public class Cub extends Rectangle {
    private Figure figure;

    public Cub(int x, int y, boolean white) {
        setX(x * Main.size);
        setY(y * Main.size);
        setWidth(Main.size);
        setHeight(Main.size);
        setFill(white ? Color.valueOf("#ffcf9f") : Color.valueOf("#d28c45"));
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public Figure getFigure() {
        return figure;
    }

    public boolean hasFigure() {
        return figure != null;
    }

}

package view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.Main;
import model.Cub;

public class CubView extends Rectangle {

    public final Cub cub = new Cub();

    public CubView(int x, int y, boolean white) {
        setX(x * Main.size);
        setY(y * Main.size);
        setWidth(Main.size);
        setHeight(Main.size);
        setFill(white ? Color.valueOf("#ffcf9f") : Color.valueOf("#d28c45"));
    }
}

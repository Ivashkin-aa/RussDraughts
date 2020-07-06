package view;

import javafx.scene.paint.Color;
import view.Main;
import model.King;

public class KingView extends FigureView {

    public final King king = new King();

    public KingView(int x, int y, boolean color) {
        figureConstructor(x, y, color, king);
        king.cir.setRadius(Main.size / 3.25);
        king.cir.setStroke(Color.YELLOW);
        king.cir.setStrokeWidth(5);
    }
}

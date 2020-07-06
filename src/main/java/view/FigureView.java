package view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Figure;

public class FigureView extends Group {

    public final Figure figure = new Figure();

    public FigureView(int x, int y, boolean color) {
        figureConstructor(x, y, color, figure);
    }

    public FigureView() {
    }

    void figureConstructor(int x, int y, boolean color, Figure workedFigure) {
        workedFigure.color = color;
        workedFigure.cir = new Circle();
        workedFigure.cir.setLayoutX(Main.size / 2 + x * Main.size);
        workedFigure.cir.setLayoutY(Main.size / 2 + y * Main.size);
        workedFigure.cir.setRadius(Main.size / 3);
        workedFigure.cir.setFill(color ? Color.WHITE : Color.BLACK);
        workedFigure.cir.setStroke(color ? Color.BLACK : Color.WHITE);
        getChildren().add(workedFigure.cir);

        setOnMousePressed(e -> {
            Figure.mouseX = e.getSceneX();
            Figure.mouseY = e.getSceneY();
        });
        setOnMouseDragged(e -> relocate(e.getSceneX(), e.getSceneY()));
    }
}

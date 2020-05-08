package model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import view.Main;

public class Figure {
    private double oldX;
    private double oldY;

    public Figure(int x, int y, String image) {
        Circle cir = new Circle();
        cir.setLayoutX(Main.size / 2 + x * Main.size);
        cir.setLayoutY(Main.size / 2 + y * Main.size);
        cir.setRadius(Main.size / 2);
        Image figure = new Image(String.valueOf(image));
        cir.setFill(new ImagePattern(figure));
        Main.root.getChildren().add(cir);
        cir.setOnMousePressed(e -> {
            oldX = cir.getTranslateX() - e.getSceneX();
            oldY = cir.getTranslateY() - e.getSceneY();
        });
        cir.setOnMouseDragged(e -> {
            cir.setTranslateX(oldX + e.getSceneX());
            cir.setTranslateY(oldY + e.getSceneY());
        });
    }
}

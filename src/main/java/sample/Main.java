package sample;

import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class Main extends Application {
    private Double oldX, oldY;

    private void buildFigure(Group root, int x, int y, String image) {
        Circle cir = new Circle(x, y, 25);
        Image figure = new Image(String.valueOf(image));
        cir.setFill(new ImagePattern(figure));
        root.getChildren().add(cir);
        cir.setOnMousePressed(e -> {
            oldX = cir.getTranslateX() - e.getSceneX();
            oldY = cir.getTranslateY() - e.getSceneY();
        });
        cir.setOnMouseDragged(e -> {
            cir.setTranslateX(oldX + e.getSceneX());
            cir.setTranslateY(oldY + e.getSceneY());
        });
    }

    private void blackTeam(Group root) {
        for (int i = 0; i < 4; i++)
            buildFigure(root, 75 + i * 100, 25, "black.png");
        for (int i = 0; i < 4; i++)
            buildFigure(root, 25 + i * 100, 75, "black.png");
        for (int i = 0; i < 4; i++)
            buildFigure(root, 75 + i * 100, 125, "black.png");
    }

    private void whiteTeam(Group root) {
        for (int i = 0; i < 4; i++)
        buildFigure(root, 25 + i * 100, 375, "white.png");
        for (int i = 0; i < 4; i++)
            buildFigure(root, 75 + i * 100, 325, "white.png");
        for (int i = 0; i < 4; i++)
            buildFigure(root, 25 + i * 100, 275, "white.png");
    }

    @Override
    public void start(Stage theStage) throws Exception {
        theStage.setTitle("Русские шашки");
        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);
        theStage.setResizable(false);

        Canvas canvas = new Canvas(400, 400);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image field = new Image("field.png");
        gc.drawImage(field, 0, 0);
        blackTeam(root);
        whiteTeam(root);

        theStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


package view;

import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Cub;
import model.Figure;

public class Main extends Application {
    public static double size = 50;
    private int rows = 8;
    private int columns = 8;
    public static Group root = new Group();
    private Cub[][] board = new Cub[rows][columns];

    private void buildBoard() {
        for (int x = 0; x != rows; x++)
            for (int y = 0; y != columns; y++) {
                board[x][y] = new Cub(x, y, (x + y) % 2 == 0);
                if (y <= 2 && (x + y) % 2 != 0) {
                    Figure black = new Figure(x, y, "black.png");
                    board[x][y].setFigure(black);
                }
                if (y >= 5 && (x + y) % 2 != 0) {
                    Figure white = new Figure(x, y, "white.png");
                    board[x][y].setFigure(white);
                }
            }
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Русские шашки");
        Scene theScene = new Scene(root, rows * size, columns * size);
        theStage.setScene(theScene);
        theStage.setResizable(false);

        buildBoard();

        theStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


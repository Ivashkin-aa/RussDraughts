package view;

import controller.Controller;
import controller.Step;
import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Cub;
import model.Figure;
import model.King;


public class Main extends Application {
    public final static double size = 50;
    private final int rows = 8;
    private final int columns = 8;
    private Stage stage;
    private Group root = new Group();
    private Cub[][] board = new Cub[rows][columns];
    private int enemyX, enemyY;

    private void buildBoard() {
        for (int x = 0; x != rows; x++)
            for (int y = 0; y != columns; y++) {
                Cub rec = new Cub(x, y, (x + y) % 2 == 0);
                board[x][y] = rec;
                root.getChildren().add(rec);
            }
    }

    private void buildFigures() {
        for (int x = 0; x != rows; x++)
            for (int y = 0; y != columns; y++) {
                if (y <= 2 && (x + y) % 2 != 0) {
                    Figure black = makeFigure(x, y, false);
                    board[x][y].setFigure(black);
                    root.getChildren().add(black);
                }
                if (y >= 5 && (x + y) % 2 != 0) {
                    Figure white = makeFigure(x, y, true);
                    board[x][y].setFigure(white);
                    root.getChildren().add(white);
                }
            }
    }

    private Controller tryMove(int newX, int newY) {
        if (board[newX][newY].hasFigure() || (newX + newY) % 2 == 0)
            return new Controller(Step.None);
        int oldX = kf(Figure.mouseX);
        int oldY = kf(Figure.mouseY);
        if (Math.abs(newX - oldX) == 1) {
            if (getColor(oldX, oldY) && (newY - oldY) == -1)
                return new Controller(Step.Move);
            if (!getColor(oldX, oldY) && (newY - oldY) == 1)
                return new Controller(Step.Move);
        }
        if (Math.abs(newX - oldX) == 2 && Math.abs(newY - oldY) == 2) {
            int enemyX = oldX + (newX - oldX) / 2;
            int enemyY = oldY + (newY - oldY) / 2;
            if (board[enemyX][enemyY].hasFigure() && getColor(oldX, oldY) != getColor(enemyX, enemyY))
                return new Controller(board[enemyX][enemyY].getFigure(), Step.Kill);
        }
        return new Controller(Step.None);
    }

    private Controller tryMoveKing(int newX, int newY) {
        if (board[newX][newY].hasFigure() || (newX + newY) % 2 == 0)
            return new Controller(Step.None);
        int oldX = kf(Figure.mouseX);
        int oldY = kf(Figure.mouseY);
        if (Math.abs(oldX - newX) == Math.abs(oldY - newY)) {
            int forX = oldX > newX ? 1 : -1;
            int forY = oldY > newY ? 1 : -1;
            int markX = oldX;
            int markY = oldY;
            while ((markX != newX && markY != newY) || !board[markX][markY].hasFigure()) {
                markX = markX + forX;
                markY = markY + forY;
            }
            if (markX == newX && markY == newY)
                return new Controller(Step.Move);
            else if (getColor(markX, markY) != getColor(oldX, oldY)) {
                Figure otherFigure = board[markX][markY].getFigure();
                enemyX = markX;
                enemyY = markY;
                while ((markX != newX && markY != newY) || !board[markX][markY].hasFigure()) {
                    markX = markX + forX;
                    markY = markY + forY;
                }
                if (markX == newX && markY == newY)
                    return new Controller(otherFigure, Step.Kill);
            }
        }
        return new Controller(Step.None);
    }

    private boolean getColor(int x, int y) {
        return board[x][y].getFigure().getColor();
    }

    private int cubCenter(double cord) {
        int newKf = (int) (cord / size);
        return (int) (newKf * size + (size / 2) - size / 3);
    }

    private int kf(double cord) {
        int newKf = (int) (cord / size);
        int cub = (int) (newKf * size + (size / 2));
        return (int) ((cub - size / 2) / size);
    }

    private Figure makeFigure(int x, int y, boolean color) {
        Figure checker = new Figure(x, y, color);
        checker.setOnMouseReleased(e -> {
            int newX = kf(e.getSceneX());
            int newY = kf(e.getSceneY());
            int oldX = kf(Figure.mouseX);
            int oldY = kf(Figure.mouseY);
            Controller result = tryMove(newX, newY);
            switch (result.getType()) {
                case None:
                    checker.relocate(cubCenter(Figure.mouseX), cubCenter(Figure.mouseY));
                    break;
                case Move:
                    checker.relocate(cubCenter(e.getSceneX()), cubCenter(e.getSceneY()));
                    if (getColor(oldX, oldY) && newY == 0) {
                        board[oldX][oldY].setFigure(null);
                        root.getChildren().remove(checker);
                        King whiteK = new King(newX, newY, true);
                        board[newX][newY].setFigure(whiteK);
                        root.getChildren().add(whiteK);
                    } else if (!getColor(oldX, oldY) && newY == 7) {
                        board[oldX][oldY].setFigure(null);
                        root.getChildren().remove(checker);
                        King blackK = new King(newX, newY, false);
                        board[newX][newY].setFigure(blackK);
                        root.getChildren().add(blackK);
                    } else {
                        board[oldX][oldY].setFigure(null);
                        board[newX][newY].setFigure(checker);
                    }
                    break;
                case Kill:
                    checker.relocate(cubCenter(e.getSceneX()), cubCenter(e.getSceneY()));
                    board[oldX][oldY].setFigure(null);
                    board[newX][newY].setFigure(checker);

                    Figure otherFigure = result.getFigure();
                    int otherX = oldX - (oldX - newX) / 2;
                    int otherY = oldY - (oldY - newY) / 2;
                    board[otherX][otherY].setFigure(null);
                    root.getChildren().remove(otherFigure);
                    break;
            }
        });
        return checker;
    }

    private King makeKing(int x, int y, boolean color) {
        King king = new King(x, y, color);
        king.setOnMouseReleased(e -> {
            int newX = kf(e.getSceneX());
            int newY = kf(e.getSceneY());
            int oldX = kf(Figure.mouseX);
            int oldY = kf(Figure.mouseY);
            Controller result = tryMoveKing(newX, newY);
            switch (result.getType()) {
                case None:
                    king.relocate(cubCenter(Figure.mouseX), cubCenter(Figure.mouseY));
                    break;
                case Move:
                    king.relocate(cubCenter(e.getSceneX()), cubCenter(e.getSceneY()));
                    board[oldX][oldY].setFigure(null);
                    board[newX][newY].setFigure(king);
                    break;
                case Kill:
                    king.relocate(cubCenter(e.getSceneX()), cubCenter(e.getSceneY()));
                    board[oldX][oldY].setFigure(null);
                    board[newX][newY].setFigure(king);

                    Figure otherFigure = result.getFigure();
                    board[enemyX][enemyY].setFigure(null);
                    root.getChildren().remove(otherFigure);
                    break;
            }
        });
        return king;
    }

    @Override
    public void start(Stage theStage) {
        stage = theStage;
        theStage.setTitle("Русские шашки");
        theStage.getIcons().add(new Image("icon.png"));
        Scene theScene = new Scene(root, rows * size, columns * size);
        Image upIcon = new Image("up.png");
        Button up = new Button("", new ImageView(upIcon));
        up.setPrefSize(50, 50);
        theStage.setScene(theScene);
        theStage.setResizable(false);

        buildBoard();
        buildFigures();
        root.getChildren().add(up);

        theStage.show();
        up.setOnAction(e -> {
            root.getChildren().clear();
            buildBoard();
            buildFigures();
            root.getChildren().add(up);
        });
        theStage.setOnCloseRequest(e -> {
            e.consume();
            closeWindow();
        });
    }

    private void closeWindow() {
        Stage close = new Stage();
        close.setTitle("Выход");
        close.getIcons().add(new Image("sure.png"));
        close.initModality(Modality.APPLICATION_MODAL);
        AnchorPane closing = new AnchorPane();

        Button exit = new Button("Да");
        Button cont = new Button("Нет");
        exit.setOnAction(e -> {
            stage.close();
            close.close();
        });
        AnchorPane.setBottomAnchor(exit, 10.0);
        AnchorPane.setLeftAnchor(exit, 35.0);
        cont.setOnAction(e -> close.close());
        AnchorPane.setBottomAnchor(cont, 10.0);
        AnchorPane.setRightAnchor(cont, 35.0);
        Label sure = new Label("Вы уверены, что хотите выйти?");
        AnchorPane.setTopAnchor(sure, 20.0);
        AnchorPane.setLeftAnchor(sure, 30.0);
        AnchorPane.setRightAnchor(sure, 30.0);

        closing.getChildren().add(sure);
        closing.getChildren().add(exit);
        closing.getChildren().add(cont);
        close.setScene(new Scene(closing, 235, 80));
        close.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


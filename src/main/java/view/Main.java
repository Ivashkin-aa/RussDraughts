package view;

import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Cub;
import model.Figure;


public class Main extends Application {
    public final static double size = 50;
    public final static int rows = 8;
    public final static int columns = 8;
    public final static char white = 'w';
    public final static char black = 'b';
    public static int contW = 12;
    public static int contB = 12;
    private Stage stage;
    public static Group root = new Group();
    public static Cub[][] board = new Cub[rows][columns];

    private void buildBoard() {
        for (int x = 0; x != rows; x++)
            for (int y = 0; y != columns; y++) {
                board[x][y] = new Cub(x, y, (x + y) % 2 == 0);
            }
    }

    private void buildFigures() {
        for (int x = 0; x != rows; x++)
            for (int y = 0; y != columns; y++) {
                if (y <= 2 && (x + y) % 2 != 0) {
                    Figure black = new Figure(x, y, "black.png");
                    black.moveFigure();
                    board[x][y].setFigure(black);
                }
                if (y >= 5 && (x + y) % 2 != 0) {
                    Figure white = new Figure(x, y, "white.png");
                    white.moveFigure();
                    board[x][y].setFigure(white);
                }
            }
    }

    @Override
    public void start(Stage theStage) {
        stage = theStage;
        theStage.setTitle("Русские шашки");
        theStage.getIcons().add(new Image("icon.png"));
        Scene theScene = new Scene(root, rows * size, columns * size);
        theStage.setScene(theScene);
        theStage.setResizable(false);

        buildBoard();
        buildFigures();

        if (contB == 0 || contW == 0)
            win();

        theStage.show();
        theStage.setOnCloseRequest(e -> {
            e.consume();
            closeWindow();
        });
    }

    private void win() {
        Stage win = new Stage();
        win.initModality(Modality.APPLICATION_MODAL);
        AnchorPane wn = new AnchorPane();

        Button newGame = new Button("Новая игра");
        Button exit = new Button("Выход");
        exit.setOnAction(e -> {
            stage.close();
            win.close();
        });
        newGame.setOnAction(e -> {
            root.getChildren().removeAll();
            buildBoard();
            buildFigures();
            win.close();
        });
        Label winLb = (contB == 0) ? new Label("Победили Белые!") : new Label("Победили Черные!");
        AnchorPane.setTopAnchor(winLb, 20.0);
        AnchorPane.setLeftAnchor(winLb, 30.0);
        AnchorPane.setRightAnchor(winLb, 30.0);

        wn.getChildren().add(newGame);
        wn.getChildren().add(exit);
        wn.getChildren().add(winLb);
        win.setScene(new Scene(wn, 235, 80));
        win.showAndWait();
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


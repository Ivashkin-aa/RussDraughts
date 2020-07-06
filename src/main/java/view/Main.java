package view;

import controller.CancelAction;
import controller.Close;
import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import controller.MainController;

public class Main extends Application {
    public final static double size = 50.0;
    public final static int rows = 8;
    public final static int columns = 8;
    public static Stage stage;
    public final static Group root = new Group();
    public static CubView[][] board = new CubView[rows][columns];
    public static int enemyX, enemyY;

    @Override
    public void start(Stage theStage) {
        stage = theStage;
        theStage.setTitle("Русские шашки");
        theStage.getIcons().add(new Image("icon.png"));
        Scene theScene = new Scene(root, rows * size, columns * size);
        Image upIcon = new Image("up.png");
        Image cancelIcon = new Image("cancel.png");
        Button up = new Button("", new ImageView(upIcon));
        Button cancel = new Button("", new ImageView(cancelIcon));
        up.setPrefSize(50, 50);
        cancel.setPrefSize(50, 50);
        cancel.relocate(350, 350);
        theStage.setScene(theScene);
        theStage.setResizable(false);

        MainController.buildBoard();
        MainController.buildFigures();
        root.getChildren().addAll(up, cancel);

        theStage.show();

        up.setOnAction(e -> {
            root.getChildren().clear();
            MainController.buildBoard();
            MainController.buildFigures();
            root.getChildren().addAll(up, cancel);
        });

        cancel.setOnAction(e -> {
            CancelAction.cancelLastAction();
        });

        theStage.setOnCloseRequest(e -> {
            e.consume();
            Close.closeWindow();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}


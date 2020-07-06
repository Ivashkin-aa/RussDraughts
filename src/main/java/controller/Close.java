package controller;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.Main;

public class Close {

    public static void closeWindow() {
        Stage close = new Stage();
        close.setTitle("Выход");
        close.getIcons().add(new Image("sure.png"));
        close.initModality(Modality.APPLICATION_MODAL);
        AnchorPane closing = new AnchorPane();

        Button exit = new Button("Да");
        Button cont = new Button("Нет");
        exit.setOnAction(e -> {
            Main.stage.close();
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
}

package model;

import controller.Controller;
import controller.Step;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import view.Main;

public class Figure extends Group {
    private double mouseX, mouseY;
    private int oldX, oldY;
    private int newX, newY;
    private String image;


    public Figure(int x, int y, String image) {
        Circle cir = new Circle();
        this.image = image;
        cir.setLayoutX(Main.size / 2 + x * Main.size);
        cir.setLayoutY(Main.size / 2 + y * Main.size);
        cir.setRadius(Main.size / 2);
        Image figure = new Image(String.valueOf(image));
        cir.setFill(new ImagePattern(figure));
        getChildren().add(cir);
        Main.root.getChildren().add(this);

        cir.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();

        });
        cir.setOnMouseDragged(e -> {
            cir.setLayoutX(e.getSceneX());
            cir.setLayoutY(e.getSceneY());
        });
        cir.setOnMouseReleased(e -> {
            newX = (int) ((cubCenter(e.getSceneX()) - Main.size / 2) / Main.size);
            newY = (int) ((cubCenter(e.getSceneY()) - Main.size / 2) / Main.size);
            oldX = getMouseX();
            oldY = getMouseY();
            Controller result = tryMove(newX, newY);
            switch (result.getType()) {
                case None:
                    cir.setLayoutX(cubCenter(mouseX));
                    cir.setLayoutY(cubCenter(mouseY));
                    break;
                case Move:
                    Main.board[oldX][oldY].setFigure(null);
                    cir.setLayoutX(cubCenter(e.getSceneX()));
                    cir.setLayoutY(cubCenter(e.getSceneY()));
                    Main.board[newX][newY].setFigure(this);
                    break;
                case Kill:
                    Main.board[oldX][oldY].setFigure(null);
                    cir.setLayoutX(cubCenter(e.getSceneX()));
                    cir.setLayoutY(cubCenter(e.getSceneY()));
                    Main.board[newX][newY].setFigure(this);

                    Figure otherFigure = result.getFigure();
                    int otherX = oldX - (oldX - newX) / 2;
                    int otherY = oldY - (oldY - newY) / 2;
                    Main.board[otherX][otherY].setFigure(null);
                    Main.root.getChildren().remove(otherFigure);
                    break;
            }
        });
    }


    private int getMouseX() {
        return (int) ((cubCenter(mouseX) - Main.size / 2) / Main.size);
    }

    private int getMouseY() {
        return (int) ((cubCenter(mouseY) - Main.size / 2) / Main.size);
    }

    private String getImage() {
        return image;
    }

    private String getColor(int x, int y) {
        return Main.board[x][y].getFigure().getImage();
    }

    private int cubCenter(double cord) {
        int newKf = (int) (cord / Main.size);
        return (int) (newKf * Main.size + (Main.size / 2));
    }

    private Controller tryMove(int newX, int newY) {
        if (Main.board[newX][newY].hasFigure() || (newX + newY) % 2 == 0)
            return new Controller(Step.None);
        oldX = getMouseX();
        oldY = getMouseY();
        if (Math.abs(newX - oldX) == 1) {
            if (getColor(oldX, oldY).equals("white.png") && (newY - oldY) == -1)
                return new Controller(Step.Move);
            if (getColor(oldX, oldY).equals("black.png") && (newY - oldY) == 1)
                return new Controller(Step.Move);
        }
        if (Math.abs(newX - oldX) == 2 && Math.abs(newY - oldY) == 2) {
            int enemyX = oldX + (newX - oldX) / 2;
            int enemyY = oldY + (newY - oldY) / 2;
            if (Main.board[enemyX][enemyY].hasFigure() && !getColor(oldX, oldY).equals(getColor(enemyX, enemyY)))
                return new Controller(Main.board[enemyX][enemyY].getFigure(), Step.Kill);
        }
        return new Controller(Step.None);
    }

}

package model;

import controller.Controller;
import controller.Step;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import view.Main;

public class Figure extends Group {
    private static double mouseX, mouseY;
    private int oldX, oldY;
    private int newX, newY;
    private int markX, markY;
    private String image;
    private Circle cir;


    public Figure(int x, int y, String image) {
        this.image = image;
        cir = new Circle();
        cir.setLayoutX(Main.size / 2 + x * Main.size);
        cir.setLayoutY(Main.size / 2 + y * Main.size);
        cir.setRadius(Main.size / 2);
        Image figure = new Image(String.valueOf(image));
        cir.setFill(new ImagePattern(figure));
        getChildren().add(cir);
        Main.root.getChildren().add(this);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();

        });
        setOnMouseDragged(e -> {
            cir.setLayoutX(e.getSceneX());
            cir.setLayoutY(e.getSceneY());
        });
    }

    //Ходы шашкой
    public void moveFigure() {
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
                    cir.setLayoutX(cubCenter(e.getSceneX()));
                    cir.setLayoutY(cubCenter(e.getSceneY()));
                    if (newY == 0 && (getColor(oldX, oldY) == Main.white)) {
                        Main.root.getChildren().remove(this);
                        King whiteKing = new King(newX, newY, "whiteD.png");
                        Main.board[newX][newY].setFigure(whiteKing);
                    }
                    if (newY == 7 && (getColor(oldX, oldY) == Main.black)) {
                        Main.root.getChildren().remove(this);
                        King blackKing = new King(newX, newY, "blackD.png");
                        Main.board[newX][newY].setFigure(blackKing);
                    } else Main.board[newX][newY].setFigure(this);
                    if (hook(oldX, oldY)) {
                        System.out.println(oldX);
                        System.out.println(oldY);
                        Main.board[newX][newY].setFigure(null);
                    } else Main.board[oldX][oldY].setFigure(null);
                    break;
                case Kill:
                    cir.setLayoutX(cubCenter(e.getSceneX()));
                    cir.setLayoutY(cubCenter(e.getSceneY()));
                    if (newY == 0 && (getColor(oldX, oldY) == Main.white)) {
                        Main.root.getChildren().remove(this);
                        King whiteKing = new King(newX, newY, "whiteD.png");
                        Main.board[newX][newY].setFigure(whiteKing);
                    }
                    if (newY == 7 && (getColor(oldX, oldY) == Main.black)) {
                        Main.root.getChildren().remove(this);
                        King blackKing = new King(newX, newY, "blackD.png");
                        Main.board[newX][newY].setFigure(blackKing);
                    } else Main.board[newX][newY].setFigure(this);
                    Main.board[oldX][oldY].setFigure(null);

                    Figure otherFigure = result.getFigure();
                    int otherX = oldX - (oldX - newX) / 2;
                    int otherY = oldY - (oldY - newY) / 2;
                    char otherColor = getColor(otherX, otherY);
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

    //Цвет фигуры
    private char getColor(int x, int y) {
        return Main.board[x][y].getFigure().getImage().charAt(0);
    }

    //Прилепить фигуру в центр клетки
    private int cubCenter(double cord) {
        int newKf = (int) (cord / Main.size);
        return (int) (newKf * Main.size + (Main.size / 2));
    }

    //Логика шашки
    public Controller tryMove(int newX, int newY) {
        if (Main.board[newX][newY].hasFigure() || (newX + newY) % 2 == 0)
            return new Controller(Step.None);
        oldX = getMouseX();
        oldY = getMouseY();
        if (Math.abs(newX - oldX) == 1) {
            if (getColor(oldX, oldY) == Main.white && (newY - oldY) == -1)
                return new Controller(Step.Move);
            if (getColor(oldX, oldY) == Main.black && (newY - oldY) == 1)
                return new Controller(Step.Move);
        }
        if (Math.abs(newX - oldX) == 2 && Math.abs(newY - oldY) == 2) {
            int enemyX = oldX + (newX - oldX) / 2;
            int enemyY = oldY + (newY - oldY) / 2;
            if (Main.board[enemyX][enemyY].hasFigure() && getColor(oldX, oldY) != getColor(enemyX, enemyY))
                return new Controller(Main.board[enemyX][enemyY].getFigure(), Step.Kill);
        }
        return new Controller(Step.None);
    }

    //Проверка на выход за границы массива
    private boolean inside(int x, int y) {
        return x < Main.columns && x >= 0 && y < Main.rows && y >= 0;
    }

    //Взятие фигуры
    public boolean hook(int oldX, int oldY) {
        char oldC = getColor(oldX, oldY);
        boolean hk = false;
        Figure oldFig = Main.board[oldX][oldY].getFigure();
        if (inside(oldX + 1, oldY + 1) &&
                Main.board[oldX + 1][oldY + 1].hasFigure() && getColor(oldX + 1, oldY + 1) != oldC)
            if (inside(oldX + 2, oldY + 2) && !Main.board[oldX + 2][oldY + 2].hasFigure())
                hk = true;
        if (inside(oldX - 1, oldY + 1) &&
                Main.board[oldX - 1][oldY + 1].hasFigure() && getColor(oldX - 1, oldY + 1) != oldC)
            if (inside(oldX - 2, oldY + 2) && !Main.board[oldX - 2][oldY + 2].hasFigure())
                hk = true;
        if (inside(oldX + 1, oldY - 1) &&
                Main.board[oldX + 1][oldY - 1].hasFigure() && getColor(oldX + 1, oldY - 1) != oldC)
            if (inside(oldX + 2, oldY - 2) && !Main.board[oldX + 2][oldY - 2].hasFigure())
                hk = true;
        if (inside(oldX - 1, oldY - 1) &&
                Main.board[oldX - 1][oldY - 1].hasFigure() && getColor(oldX - 1, oldY - 1) != oldC)
            if (inside(oldX - 2, oldY - 2) && !Main.board[oldX - 2][oldY - 2].hasFigure())
                hk = true;
        if (hk) {
            Main.board[oldX][oldY].setFigure(null);
            Main.root.getChildren().remove(oldFig);
        }
        return hk;
    }

    //Ходы дамкой
    void moveKing() {
        cir.setOnMouseReleased(e -> {
            newX = (int) ((cubCenter(e.getSceneX()) - Main.size / 2) / Main.size);
            newY = (int) ((cubCenter(e.getSceneY()) - Main.size / 2) / Main.size);
            oldX = getMouseX();
            oldY = getMouseY();
            Controller result = tryMoveKing(newX, newY);
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
                    char otherColor = getColor(markX, markY);

                    Main.board[markX][markY].setFigure(null);
                    Main.root.getChildren().remove(otherFigure);
                    break;
            }
        });
    }

    //Логика дамки
    public Controller tryMoveKing(int newX, int newY) {
        if (Main.board[newX][newY].hasFigure() || (newX + newY) % 2 == 0)
            return new Controller(Step.None);
        if (Math.abs(newX - oldX) == Math.abs(newY - oldY)) {
            /*int nowX = 0, nowY = 0;
            if (oldX > newX && oldY > newY) {
                while (!Main.board[nowX][nowY].hasFigure() || nowX < newX || nowY < newY) {
                    nowX = oldX - 1;
                    nowY = oldY - 1;
                }
                if (Main.board[nowX][nowY].hasFigure()) {
                    if (getColor(nowX, nowY) == getColor(oldX, oldY))
                        return new Controller(Step.None);
                    else {
                        int markX = nowX;
                        int markY = nowY;
                        nowX--;
                        nowY--;
                        while (!Main.board[nowX][nowY].hasFigure() || nowX < newX || nowY < newY) {
                            nowX--;
                            nowY--;
                        }
                        if (Main.board[nowX][nowY].hasFigure())
                            return new Controller(Step.None);
                        else return new Controller(Main.board[markX][markY].getFigure(), Step.Kill);
                    }
                } else return new Controller(Step.Move);
            }
            if (oldX > newX && oldY < newY) {
                while (!Main.board[nowX][nowY].hasFigure() || nowX < newX || nowY < newY) {
                    nowX = oldX - 1;
                    nowY = oldY + 1;
                }
                if (Main.board[nowX][nowY].hasFigure()) {
                    if (getColor(nowX, nowY) == getColor(oldX, oldY))
                        return new Controller(Step.None);
                    else {
                        int markX = nowX;
                        int markY = nowY;
                        nowX--;
                        nowY++;
                        while (!Main.board[nowX][nowY].hasFigure() || nowX < newX || nowY < newY) {
                            nowX--;
                            nowY++;
                        }
                        if (Main.board[nowX][nowY].hasFigure())
                            return new Controller(Step.None);
                        else return new Controller(Main.board[markX][markY].getFigure(), Step.Kill);
                    }
                } else return new Controller(Step.Move);
            }
            if (oldX < newX && oldY > newY) {
                while (!Main.board[nowX][nowY].hasFigure() || nowX < newX || nowY < newY) {
                    nowX = oldX + 1;
                    nowY = oldY - 1;
                }
                if (Main.board[nowX][nowY].hasFigure()) {
                    if (getColor(nowX, nowY) == getColor(oldX, oldY))
                        return new Controller(Step.None);
                    else {
                        int markX = nowX;
                        int markY = nowY;
                        nowX++;
                        nowY--;
                        while (!Main.board[nowX][nowY].hasFigure() || nowX < newX || nowY < newY) {
                            nowX++;
                            nowY--;
                        }
                        if (Main.board[nowX][nowY].hasFigure())
                            return new Controller(Step.None);
                        else return new Controller(Main.board[markX][markY].getFigure(), Step.Kill);
                    }
                } else return new Controller(Step.Move);
            }
            if (oldX < newX && oldY < newY) {
                while (!Main.board[nowX][nowY].hasFigure() || nowX < newX || nowY < newY) {
                    nowX = oldX + 1;
                    nowY = oldY + 1;
                }
                if (Main.board[nowX][nowY].hasFigure()) {
                    if (getColor(nowX, nowY) == getColor(oldX, oldY))
                        return new Controller(Step.None);
                    else {
                        int markX = nowX;
                        int markY = nowY;
                        nowX++;
                        nowY++;
                        while (!Main.board[nowX][nowY].hasFigure() || nowX < newX || nowY < newY) {
                            nowX++;
                            nowY++;
                        }
                        if (Main.board[nowX][nowY].hasFigure())
                            return new Controller(Step.None);
                        else return new Controller(Main.board[markX][markY].getFigure(), Step.Kill);
                    }
                } else return new Controller(Step.Move);
            }*/
            if (newX > oldX)
                markX = newX - 1;
            else
                markX = newX + 1;
            if (newY > oldY)
                markY = newY - 1;
            else markY = newY + 1;
            if (Main.board[markX][markY].hasFigure() && getColor(oldX, oldY) != getColor(markX, markY))
                return new Controller(Main.board[markX][markY].getFigure(), Step.Kill);
            else return new Controller(Step.Move);
        }
        return new Controller(Step.None);
    }
}


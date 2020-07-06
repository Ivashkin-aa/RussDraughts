package controller;

import view.Main;
import view.CubView;
import view.FigureView;
import view.KingView;

public class CancelAction {
    public static int oldX = -1;
    public static int oldY = -1;
    public static int currX, currY;
    public static boolean isKing;

    public static void cancelLastAction() {
        if (oldX != -1 && oldY != -1) {
            boolean color = MainController.getColor(currX, currY);
            CubView cub = new CubView(currX, currY, false);
            Main.root.getChildren().add(0, cub);
            Main.root.getChildren().remove(Main.board[currX][currY].cub.figure);
            Main.board[currX][currY].cub.setFigure(null);
            if (isKing) {
                KingView king = MainController.makeKing(oldX, oldY, !color);
                Main.board[oldX][oldY].cub.setFigure(king);
                Main.root.getChildren().add(king);
            } else {
                FigureView fig = MainController.makeFigure(oldX, oldY, color);
                Main.board[oldX][oldY].cub.setFigure(fig);
                Main.root.getChildren().add(fig);
            }
            if (Math.abs(currX - oldX) == 2) {
                FigureView figure = MainController
                        .makeFigure((oldX + currX)/2, (oldY + currY)/2, !color);
                Main.board[(oldX + currX)/2][(oldY + currY)/2].cub.setFigure(figure);
                Main.root.getChildren().add(figure);
            }
            oldX = -1;
            oldY = -1;
        }
    }
}

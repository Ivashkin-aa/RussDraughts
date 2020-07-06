package controller;

import view.*;
import model.Figure;
import model.King;

import static view.Main.*;

public class MainController {
    public static void buildBoard() {
        for (int x = 0; x != rows; x++)
            for (int y = 0; y != columns; y++) {
                CubView rec = new CubView(x, y, (x + y) % 2 == 0);
                board[x][y] = rec;
                root.getChildren().add(rec);
            }
    }

    public static void buildFigures() {
        for (int x = 0; x != rows; x++)
            for (int y = 0; y != columns; y++) {
                if (y <= 2 && (x + y) % 2 != 0) {
                    FigureView black = makeFigure(x, y, false);
                    board[x][y].cub.setFigure(black);
                    root.getChildren().add(black);
                }
                if (y >= 5 && (x + y) % 2 != 0) {
                    FigureView white = makeFigure(x, y, true);
                    board[x][y].cub.setFigure(white);
                    root.getChildren().add(white);
                }
            }
    }

    public static Move tryMove(int newX, int newY) {
        if (board[newX][newY].cub.hasFigure() || (newX + newY) % 2 == 0)
            return new Move(Step.None);
        int oldX = kf(Figure.mouseX);
        int oldY = kf(Figure.mouseY);
        CancelAction.oldX = oldX;
        CancelAction.oldY = oldY;
        CancelAction.currX = newX;
        CancelAction.currY = newY;
        CancelAction.isKing = false;
        if (Math.abs(newX - oldX) == 1) {
            if (getColor(oldX, oldY) && (newY - oldY) == -1)
                return new Move(Step.Move);
            if (!getColor(oldX, oldY) && (newY - oldY) == 1)
                return new Move(Step.Move);
        }
        if (Math.abs(newX - oldX) == 2 && Math.abs(newY - oldY) == 2) {
            int enemyX = oldX + (newX - oldX) / 2;
            int enemyY = oldY + (newY - oldY) / 2;
            if (board[enemyX][enemyY].cub.hasFigure() && getColor(oldX, oldY) != getColor(enemyX, enemyY))
                return new Move(board[enemyX][enemyY].cub.getFigure(), Step.Kill);
        }
        return new Move(Step.None);
    }

    public static Move tryMoveKing(int newX, int newY) {
        if (board[newX][newY].cub.hasFigure() || (newX + newY) % 2 == 0)
            return new Move(Step.None);
        int oldX = kf(King.mouseX);
        int oldY = kf(King.mouseY);
        CancelAction.oldX = oldX;
        CancelAction.oldY = oldY;
        CancelAction.currX = newX;
        CancelAction.currY = newY;
        CancelAction.isKing = true;
        if (Math.abs(oldX - newX) == Math.abs(oldY - newY)) {
            int forX = oldX > newX ? 1 : -1;
            int forY = oldY > newY ? 1 : -1;
            int markX = oldX;
            int markY = oldY;
            while ((markX != newX && markY != newY)) {
                markX = markX - forX;
                markY = markY - forY;
                if (board[markX][markY].cub.hasFigure()) {
                    if (getColor(markX, markY) == getColor(oldX, oldY))
                        return new Move(Step.None);
                    else {
                        enemyX = markX;
                        enemyY = markY;
                        int markX1 = markX;
                        int markY1 = markY;
                        while (markX1 != newX && markY1 != newY) {
                            markX1 = markX1 - forX;
                            markY1 = markY1 - forY;
                            if (board[markX1][markY1].cub.hasFigure())
                                return new Move(Step.None);
                        }
                        return new Move(board[enemyX][enemyY].cub.getFigure(), Step.Kill);
                    }
                }
            }
            return new Move(Step.Move);
        }
        return new Move(Step.None);
    }

    public static boolean getColor(int x, int y) {
        return board[x][y].cub.getFigure().figure.getColor();
    }

    private static int cubCenter(double cord) {
        int newKf = (int) (cord / size);
        return (int) (newKf * size + (size / 2) - size / 3);
    }

    private static int kf(double cord) {
        int newKf = (int) (cord / size);
        int cub = (int) (newKf * size + (size / 2));
        return (int) ((cub - size / 2) / size);
    }

    private static void checkForKing(int oldX, int oldY, int newX, int newY) {
        FigureView checker = board[oldX][oldY].cub.getFigure();
        if (getColor(oldX, oldY) && newY == 0) {
            root.getChildren().remove(checker);
            KingView whiteK = makeKing(newX, newY, true);
            board[newX][newY].cub.setFigure(whiteK);
            root.getChildren().add(whiteK);
        } else if (!getColor(oldX, oldY) && newY == 7) {
            root.getChildren().remove(checker);
            KingView blackK = makeKing(newX, newY, false);
            board[newX][newY].cub.setFigure(blackK);
            root.getChildren().add(blackK);
        } else {
            board[newX][newY].cub.setFigure(checker);
        }
        board[oldX][oldY].cub.setFigure(null);
    }

    public static FigureView makeFigure(int x, int y, boolean color) {
        FigureView checker = new FigureView(x, y, color);
        checker.setOnMouseReleased(e -> {
            int newX = kf(e.getSceneX());
            int newY = kf(e.getSceneY());
            int oldX = kf(Figure.mouseX);
            int oldY = kf(Figure.mouseY);
            Move result = tryMove(newX, newY);
            switch (result.getType()) {
                case None:
                    checker.relocate(cubCenter(Figure.mouseX), cubCenter(Figure.mouseY));
                    break;
                case Move:
                    checker.relocate(cubCenter(e.getSceneX()), cubCenter(e.getSceneY()));
                    checkForKing(oldX, oldY, newX, newY);
                    break;
                case Kill:
                    checker.relocate(cubCenter(e.getSceneX()), cubCenter(e.getSceneY()));
                    checkForKing(oldX, oldY, newX, newY);

                    FigureView otherFigure = result.getFigure();
                    int otherX = oldX - (oldX - newX) / 2;
                    int otherY = oldY - (oldY - newY) / 2;
                    board[otherX][otherY].cub.setFigure(null);
                    root.getChildren().remove(otherFigure);
                    break;
            }
        });
        return checker;
    }

    public static KingView makeKing(int x, int y, boolean color) {
        KingView king = new KingView(x, y, color);
        king.setOnMouseReleased(e -> {
            int newX = kf(e.getSceneX());
            int newY = kf(e.getSceneY());
            int oldX = kf(King.mouseX);
            int oldY = kf(King.mouseY);
            Move result = tryMoveKing(newX, newY);
            switch (result.getType()) {
                case None:
                    king.relocate(cubCenter(King.mouseX), cubCenter(King.mouseY));
                    break;
                case Move:
                    king.relocate(cubCenter(e.getSceneX()), cubCenter(e.getSceneY()));
                    board[oldX][oldY].cub.setFigure(null);
                    board[newX][newY].cub.setFigure(king);
                    break;
                case Kill:
                    king.relocate(cubCenter(e.getSceneX()), cubCenter(e.getSceneY()));
                    board[oldX][oldY].cub.setFigure(null);
                    board[newX][newY].cub.setFigure(king);

                    FigureView otherFigure = result.getFigure();
                    board[enemyX][enemyY].cub.setFigure(null);
                    root.getChildren().remove(otherFigure);
                    break;
            }
        });
        return king;
    }
}

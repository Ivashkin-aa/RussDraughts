import controller.MainController;
import org.junit.jupiter.api.Test;
import view.*;

import static org.junit.jupiter.api.Assertions.*;

class DraughtsTest {

    @Test
    void moveFigure() {
        FigureView white = MainController.makeFigure(3, 4, true);
        Main.board[3][4].cub.setFigure(white);
        FigureView black = MainController.makeFigure(4, 3, false);
        Main.board[4][3].cub.setFigure(black);
        assertEquals(new Move(Step.None), MainController.tryMove(3, 3));
        assertEquals(new Move(Step.Move), MainController.tryMove(2, 3));
        assertEquals(new Move(Step.Kill), MainController.tryMove(5, 2));
    }

    @Test
    void moveKing() {
        KingView blackKing = MainController.makeKing(1, 6, false);
        Main.board[1][6].cub.setFigure(blackKing);
        FigureView white1 = MainController.makeFigure(4, 3, true);
        Main.board[4][3].cub.setFigure(white1);
        FigureView white2 = MainController.makeFigure(5, 2, true);
        Main.board[5][2].cub.setFigure(white2);
        assertEquals(new Move(Step.None), MainController.tryMoveKing(6, 1));
        assertEquals(new Move(Step.Move), MainController.tryMoveKing(3, 4));
        Main.board[5][2].cub.setFigure(null);
        assertEquals(new Move(Step.Kill), MainController.tryMoveKing(6, 1));
    }
}

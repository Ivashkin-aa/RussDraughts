import controller.Controller;
import controller.Step;
import model.Figure;
import model.King;
import org.junit.jupiter.api.Test;
import view.Main;

import static org.junit.jupiter.api.Assertions.*;

class DraughtsTest {

    @Test
    void tryMoveChecker() {
        Figure black = new Figure(3, 4, "black.png");
        Main.board[3][4].setFigure(black);
        Figure white = new Figure(4, 3, "white.png");
        Main.board[4][3].setFigure(white);
        assertEquals(new Controller(Step.None), black.tryMove(3, 3));
        assertEquals(new Controller(Step.Kill), black.tryMove(5, 2));
        assertEquals(new Controller(Step.Move), black.tryMove(2, 5));
        assertTrue(black.hook(3, 4));
    }

    @Test
    void tryMoveKing() {
        King blackD = new King(4, 0, "blackD.png");
        Main.board[4][0].setFigure(blackD);
        Figure black = new Figure(6, 2, "black.png");
        Main.board[6][2].setFigure(black);
        Figure white = new Figure(4, 6, "white.png");
        Main.board[4][6].setFigure(white);
        assertEquals(new Controller(Step.None), blackD.tryMoveKing(6, 2));
        assertEquals(new Controller(Step.Move), blackD.tryMoveKing(1, 3));
        Main.board[1][3].setFigure(blackD);
        assertEquals(new Controller(Step.Kill), blackD.tryMoveKing(5, 7));
    }

}

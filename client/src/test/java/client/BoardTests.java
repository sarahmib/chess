package client;

import chess.ChessBoard;
import ui.OutputChessboard;

import org.junit.jupiter.api.Test;

public class BoardTests {
    @Test
    public void testCreateBoard() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        OutputChessboard.main(board.board);
    }
}

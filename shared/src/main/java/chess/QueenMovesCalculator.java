package chess;

import java.util.Collection;

public class QueenMovesCalculator {

    public static void pieceMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        MovesCalculator.moveUp(board, position, validMoves);
        MovesCalculator.moveUpRight(board, position, validMoves);
        MovesCalculator.moveRight(board, position, validMoves);
        MovesCalculator.moveDownRight(board, position, validMoves);
        MovesCalculator.moveDown(board, position, validMoves);
        MovesCalculator.moveDownLeft(board, position, validMoves);
        MovesCalculator.moveLeft(board, position, validMoves);
        MovesCalculator.moveUpLeft(board, position, validMoves);
    }
}

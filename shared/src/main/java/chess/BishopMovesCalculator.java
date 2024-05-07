package chess;

import java.util.Collection;

public class BishopMovesCalculator {

    public static void pieceMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        MovesCalculator.moveUpLeft(board, position, validMoves);
        MovesCalculator.moveUpRight(board, position, validMoves);
        MovesCalculator.moveDownLeft(board, position, validMoves);
        MovesCalculator.moveDownRight(board, position, validMoves);
    }
}

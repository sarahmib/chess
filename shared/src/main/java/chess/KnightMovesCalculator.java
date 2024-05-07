package chess;

import java.util.Collection;

public class KnightMovesCalculator {

    public static void pieceMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int row = position.getRow();
        int col = position.getColumn();

        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row - 2, col - 1))) {
            MovesCalculator.addMove(position, new ChessPosition(row - 2, col - 1), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row - 2, col + 1))) {
            MovesCalculator.addMove(position, new ChessPosition(row - 2, col + 1), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row - 1, col - 2))) {
            MovesCalculator.addMove(position, new ChessPosition(row - 1, col - 2), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row - 1, col + 2))) {
            MovesCalculator.addMove(position, new ChessPosition(row - 1, col + 2), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row + 1, col - 2))) {
            MovesCalculator.addMove(position, new ChessPosition(row + 1, col - 2), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row + 1, col + 2))) {
            MovesCalculator.addMove(position, new ChessPosition(row + 1, col + 2), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row + 2, col - 1))) {
            MovesCalculator.addMove(position, new ChessPosition(row + 2, col - 1), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row + 2, col + 1))) {
            MovesCalculator.addMove(position, new ChessPosition(row + 2, col + 1), validMoves);
        }
    }
}
